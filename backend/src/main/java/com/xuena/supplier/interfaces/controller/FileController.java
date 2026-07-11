package com.xuena.supplier.interfaces.controller;

import com.xuena.supplier.application.service.FileService;
import com.xuena.supplier.domain.entity.FileDO;
import com.xuena.supplier.interfaces.vo.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/file")
@Tag(name = "文件管理", description = "文件上传、下载、管理接口")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    @Operation(summary = "上传文件", description = "上传普通文件或模板文件")
    public ResultVO<FileDO> upload(
            @Parameter(description = "文件") @RequestParam("file") MultipartFile file,
            @Parameter(description = "文件类型（NORMAL-普通文件，TEMPLATE-模板文件）") @RequestParam(value = "fileType", defaultValue = "NORMAL") String fileType,
            @Parameter(description = "业务类型（SUPPLIER_POOL-供应商池，DEPARTMENT_SCORE-部门打分，ASSESSMENT-考核管理，MEETING_NOTE-会议纪要）") @RequestParam(value = "businessType", required = false) String businessType,
            @Parameter(description = "文件描述") @RequestParam(value = "description", required = false) String description) throws IOException {
        FileDO result = fileService.upload(file, fileType, businessType, description);
        return ResultVO.success(result);
    }

    @GetMapping("/download/{storeKey}")
    @Operation(summary = "根据storeKey下载文件", description = "通过存储键下载文件")
    public void downloadByStoreKey(
            @Parameter(description = "存储键") @PathVariable String storeKey,
            HttpServletResponse response) throws IOException {
        FileDO fileDO = fileService.getByStoreKey(storeKey);
        if (fileDO == null) {
            response.sendError(404, "文件不存在");
            return;
        }
        downloadFile(fileDO, response);
    }

    @GetMapping("/download")
    @Operation(summary = "根据文件名下载文件", description = "通过原始文件名下载文件")
    public void downloadByFileName(
            @Parameter(description = "原始文件名") @RequestParam("fileName") String fileName,
            HttpServletResponse response) throws IOException {
        FileDO fileDO = fileService.getByFileName(fileName);
        if (fileDO == null) {
            response.sendError(404, "文件不存在");
            return;
        }
        downloadFile(fileDO, response);
    }

    @GetMapping
    @Operation(summary = "查询文件列表", description = "查询文件列表，支持按类型和名称筛选")
    public ResultVO<List<FileDO>> list(
            @Parameter(description = "文件类型") @RequestParam(value = "fileType", required = false) String fileType,
            @Parameter(description = "文件名") @RequestParam(value = "fileName", required = false) String fileName,
            @Parameter(description = "业务类型") @RequestParam(value = "businessType", required = false) String businessType) {
        List<FileDO> list = fileService.list(fileType, fileName, businessType);
        return ResultVO.success(list);
    }

    @GetMapping("/template/{businessType}")
    @Operation(summary = "按业务类型获取模板列表", description = "获取指定业务类型的模板文件列表")
    public ResultVO<List<FileDO>> getTemplatesByBusinessType(
            @Parameter(description = "业务类型") @PathVariable String businessType) {
        List<FileDO> list = fileService.getTemplatesByBusinessType(businessType);
        return ResultVO.success(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询文件详情", description = "根据ID查询文件详情")
    public ResultVO<FileDO> getById(
            @Parameter(description = "文件ID") @PathVariable String id) {
        FileDO fileDO = fileService.getById(id);
        return ResultVO.success(fileDO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除文件", description = "根据ID删除文件")
    public ResultVO<Void> delete(
            @Parameter(description = "文件ID") @PathVariable String id) {
        fileService.delete(id);
        return ResultVO.success();
    }

    private void downloadFile(FileDO fileDO, HttpServletResponse response) throws IOException {
        Path filePath = Paths.get(fileDO.getFilePath());
        if (!Files.exists(filePath)) {
            response.sendError(404, "文件不存在");
            return;
        }

        response.setContentType(fileDO.getContentType() != null ? fileDO.getContentType() : "application/octet-stream");
        response.setCharacterEncoding("utf-8");
        String encodedFileName = URLEncoder.encode(fileDO.getFileName(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=UTF-8''" + encodedFileName);

        Files.copy(filePath, response.getOutputStream());
    }
}