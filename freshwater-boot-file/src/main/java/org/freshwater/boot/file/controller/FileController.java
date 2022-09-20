package org.freshwater.boot.file.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.freshwater.boot.common.model.ResponseCode;
import org.freshwater.boot.common.model.ResponseModel;
import org.freshwater.boot.common.utils.ResponseModelUtils;
import org.freshwater.boot.common.utils.ServletResponseUtils;
import org.freshwater.boot.file.entity.FileEntity;
import org.freshwater.boot.file.service.FileService;
import org.freshwater.boot.file.service.FileUploadService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 文件 前端控制器
 * </p>
 *
 * @author tuxuchen
 * @since 2022-08-03
 */
@Api(value = "文件管理", tags = {"文件管理"})
@RestController
@RequestMapping("/v1/files")
public class FileController {

  @Autowired
  private FileService fileService;
  @Autowired
  private FileUploadService fileUploadService;

  /**
   * 上传文件,支持多个文件上传
   *
   * @param subSystem 子系统名称,主要是为上传的文件做分类处理,通常会拼接在文件路径的最前面
   * @param effective 文件有效天数,如果不传或时间小于等于0,则表示永久有效
   * @param files     文件
   * @return
   */
  @PostMapping("upload")
  @ApiOperation(value = "上传文件", notes = "支持多个文件上传")
  public ResponseModel upload(@RequestParam @ApiParam("子系统名称,主要是为上传的文件做分类处理,通常会拼接在文件路径的最前面") String subSystem,
                              @ApiParam("文件有效天数,如果不传或时间小于等于0,则表示永久有效") Integer effective,
                              @RequestParam("file") @ApiParam("文件") MultipartFile[] files) {
    List<FileEntity> fileList = fileUploadService.upload(subSystem, effective, files);
    return ResponseModelUtils.success(fileList);
  }

  /**
   * 文件下载服务
   *
   * @param path
   * @param filename
   * @return
   */
  @GetMapping("download")
  @ApiOperation("文件下载服务")
  public void download(HttpServletRequest request, HttpServletResponse response,
                       @ApiParam("文件路径,可不传") String path,
                       @ApiParam("文件名,注意是文件系统重命名的文件") String filename) throws IOException {
    byte[] bytes = fileService.read(path, filename);
    if(bytes == null) {
      ServletResponseUtils.writeJson(response, ResponseModelUtils.failure(ResponseCode.E5001, "文件不存在", ""));
      return;
    }
    String originalFilename = filename;
    FileEntity file = fileUploadService.findByPathAndFilename(path, filename);
    if(file != null) {
      originalFilename = file.getOriginalFilename();
    }
    ServletResponseUtils.writeFile(request, response, bytes, originalFilename);
  }

  /**
   * 图片预览服务
   *
   * @param path
   * @param filename
   * @return
   */
  @GetMapping("image/view")
  @ApiOperation("图片预览服务")
  public void viewImage(HttpServletRequest request, HttpServletResponse response,
                        @ApiParam("文件路径,可不传") String path,
                        @ApiParam("文件名,注意是文件系统重命名的文件") String filename) throws IOException {
    byte[] bytes = fileService.read(path, filename);
    if(bytes == null) {
      ServletResponseUtils.writeJson(response, ResponseModelUtils.failure(ResponseCode.E5001, "文件不存在", ""));
      return;
    }
    ServletResponseUtils.writeImage(request, response, bytes, null);
  }

  /**
   * 文件预览服务
   * @param request
   * @param response
   * @param path
   * @param filename
   * @throws IOException
   */
  @GetMapping("file/view")
  @ApiOperation("文件预览服务")
  public void viewFile(HttpServletRequest request, HttpServletResponse response,
                       @ApiParam("文件路径,可不传") String path,
                       @ApiParam("文件名,注意是文件系统重命名的文件") String filename) throws IOException {
    byte[] bytes = fileService.read(path, filename);
    if(bytes == null) {
      ServletResponseUtils.writeJson(response, ResponseModelUtils.failure(ResponseCode.E5001, "文件不存在", ""));
      return;
    }
    ServletResponseUtils.writeFile(request, response, bytes, null);
  }

  /**
   * 删除文件
   * @param path
   * @param filename
   */
  @DeleteMapping("deleteFile")
  @ApiOperation(value = "删除文件", notes = "根据保存的文件路径和文件名删除")
  public ResponseModel deleteFile(@ApiParam("文件路径,可不传") String path,
                                  @ApiParam("文件名,注意是文件系统重命名的文件") String filename) {
    fileUploadService.deleteFile(path, filename);
    return ResponseModelUtils.success();
  }

}
