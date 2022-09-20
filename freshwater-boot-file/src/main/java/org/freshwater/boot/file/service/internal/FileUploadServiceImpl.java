package org.freshwater.boot.file.service.internal;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.freshwater.boot.common.utils.FileUtils;
import org.freshwater.boot.file.entity.FileEntity;
import org.freshwater.boot.file.mapper.FileMapper;
import org.freshwater.boot.file.service.FileService;
import org.freshwater.boot.file.service.FileUploadService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 文件上传服务实现
 * 本服务主要为了给前端提供文件上传的接口服务
 * 通过本服务上传的文件,会将文件信息保存的数据库中
 * @author tuxuchen
 * @date 2022/8/3 14:23
 */
public class FileUploadServiceImpl extends ServiceImpl<FileMapper, FileEntity> implements FileUploadService {

  /**
   * 默认有效时间
   */
  private static final LocalDate DEFAULT_EFFECTIVE_DATE = LocalDate.of(3999, 1, 1);

  @Autowired
  private FileService fileService;

  @Override
  @Transactional
  public List<FileEntity> upload(String subSystem, Integer effective, MultipartFile[] files) {
    this.uploadValidation(subSystem, files);
    List<FileEntity> fileList = Lists.newArrayList();
    LocalDate effectiveDate = this.getEffectiveDate(effective);
    for (MultipartFile file : files) {
      String originalFilename = file.getOriginalFilename();
      String fileSuffix = FileUtils.getFileSuffix(originalFilename);
      String randomPath = randomPath();
      String path = StringUtils.join("/", subSystem, randomPath);
      String filename = StringUtils.join(UUID.randomUUID().toString(), ".", fileSuffix);
      try {
        fileService.save(path, filename, file.getInputStream());
      } catch (IOException e) {
        log.error(e.getMessage(), e);
        throw new RuntimeException("上传文件失败!", e);
      }
      FileEntity fileEntity = this.create(path, filename, originalFilename, fileSuffix, effectiveDate);
      fileList.add(fileEntity);
    }
    return fileList;
  }

  @Override
  public List<FileEntity> findByIds(List<String> ids) {
    if (CollectionUtils.isEmpty(ids)){
      return Lists.newArrayList();
    }
    return baseMapper.selectBatchIds(ids);
  }

  @Override
  public FileEntity findByPathAndFilename(String path, String filename) {
    if(StringUtils.isBlank(filename)) {
      return null;
    }
    path = ObjectUtils.defaultIfNull(path, "");
    return baseMapper.selectByPathAndFilename(path, filename);
  }

  @Override
  @Transactional
  public void deleteFile(String path, String filename) {
    Validate.notBlank(filename, "文件名不能为空");
    path = ObjectUtils.defaultIfNull(path, "");
    FileEntity file = baseMapper.selectByPathAndFilename(path, filename);
    if(file != null) {
      baseMapper.deleteById(file.getId());
    }
    fileService.delete(path, filename);
  }

  /**
   * 上传前数据校验
   * @param subSystem
   * @param files
   */
  private void uploadValidation(String subSystem, MultipartFile[] files) {
    Validate.notBlank(subSystem, "子系统名称不能为空");
    Validate.notEmpty(files, "上传文件内容为空");
    for (MultipartFile file : files) {
      String originalFilename = file.getOriginalFilename();
      long size = file.getSize();
      fileService.valid(originalFilename, size);
    }
  }


  /**
   * 计算有效时间
   * @param effective
   * @return
   */
  private LocalDate getEffectiveDate(Integer effective) {
    if(effective != null && effective > 0) {
      LocalDate date = LocalDate.now();
      date.plusDays(effective);
      return date;
    }
    return DEFAULT_EFFECTIVE_DATE;
  }

  /**
   * 保存文件信息
   * @param path
   * @param filename
   * @param originalFilename
   * @param effective
   * @return
   */
  private FileEntity create(String path, String filename, String originalFilename, String fileSuffix, LocalDate effective) {
    FileEntity file = new FileEntity();
    file.setPath(path);
    file.setFilename(filename);
    file.setOriginalFilename(originalFilename);
    file.setEffectiveDate(effective);
    file.setSuffix(fileSuffix);
    file.setCreator("");
    file.setCreateTime(LocalDateTime.now());
    baseMapper.insert(file);
    return file;
  }

  /**
   * 生成一个随机的文件路径
   * @return
   */
  public static String randomPath() {
    LocalDate localDate = LocalDate.now();
    Random random = new Random();
    String date = localDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
    int i = random.nextInt(10);
    return StringUtils.join("/", date, "/", i);
  }

}
