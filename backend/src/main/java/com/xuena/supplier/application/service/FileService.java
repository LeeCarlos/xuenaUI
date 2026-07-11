package com.xuena.supplier.application.service;

import com.xuena.supplier.domain.entity.FileDO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {

    FileDO upload(MultipartFile file, String fileType, String description) throws IOException;

    FileDO getById(String id);

    FileDO getByStoreKey(String storeKey);

    FileDO getByFileName(String fileName);

    List<FileDO> list(String fileType, String fileName);

    void delete(String id);
}