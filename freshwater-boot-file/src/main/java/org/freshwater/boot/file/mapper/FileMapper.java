package org.freshwater.boot.file.mapper;

import org.apache.ibatis.annotations.Param;
import org.freshwater.boot.file.entity.FileEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 * 文件 Mapper 接口
 * </p>
 *
 * @author tuxuchen
 * @since 2022-08-03 14:03
 */
public interface FileMapper extends BaseMapper<FileEntity> {

  /**
   * 读取文件的相关信息
   * @param path
   * @param filename
   * @return
   */
  FileEntity selectByPathAndFilename(@Param("path") String path, @Param("filename") String filename);

}
