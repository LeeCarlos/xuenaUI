package com.xuena.supplier.application.service.impl;

import com.xuena.supplier.application.service.FileService;
import com.xuena.supplier.domain.entity.FileDO;
import com.xuena.supplier.infrastructure.mapper.FileMapper;
import com.xuena.supplier.infrastructure.util.IdGenerator;
import com.xuena.supplier.infrastructure.util.UserContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class FileServiceImpl implements FileService {

    private final FileMapper fileMapper;
    private final IdGenerator idGenerator;

    @Value("${file.upload.path:uploads}")
    private String uploadPath;

    public FileServiceImpl(FileMapper fileMapper, IdGenerator idGenerator) {
        this.fileMapper = fileMapper;
        this.idGenerator = idGenerator;
    }

    @Override
    public FileDO upload(MultipartFile file, String fileType, String description) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String storeKey = idGenerator.generateId();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        String storedFileName = storeKey + extension;

        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }

        Path filePath = uploadDir.resolve(storedFileName);
        file.transferTo(filePath.toFile());

        FileDO fileDO = new FileDO();
        fileDO.setId(idGenerator.generateId());
        fileDO.setFileName(originalFilename);
        fileDO.setStoreKey(storeKey);
        fileDO.setFileType(fileType);
        fileDO.setFilePath(filePath.toString());
        fileDO.setFileSize(file.getSize());
        fileDO.setContentType(file.getContentType());
        fileDO.setDescription(description);
        fileDO.setCreateId(UserContext.getUserId());
        fileDO.setCreateName(UserContext.getUserName());

        fileMapper.insert(fileDO);
        return fileDO;
    }

    @Override
    public FileDO getById(String id) {
        return fileMapper.selectById(id);
    }

    @Override
    public FileDO getByStoreKey(String storeKey) {
        return fileMapper.selectByStoreKey(storeKey);
    }

    @Override
    public FileDO getByFileName(String fileName) {
        return fileMapper.selectByFileName(fileName);
    }

    @Override
    public List<FileDO> list(String fileType, String fileName) {
        return fileMapper.selectList(fileType, fileName);
    }

    @Override
    public void delete(String id) {
        FileDO fileDO = fileMapper.selectById(id);
        if (fileDO != null) {
            try {
                Path filePath = Paths.get(fileDO.getFilePath());
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
            }
            fileMapper.delete(id);
        }
    }
}