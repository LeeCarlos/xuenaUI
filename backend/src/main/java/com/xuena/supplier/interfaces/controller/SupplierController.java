package com.xuena.supplier.interfaces.controller;

import com.xuena.supplier.domain.entity.SupplierPoolDO;
import com.xuena.supplier.domain.entity.FileDO;
import com.xuena.supplier.interfaces.vo.SupplierTemplateVO;
import com.xuena.supplier.interfaces.vo.SupplierExportVO;
import com.xuena.supplier.application.service.SupplierPoolService;
import com.xuena.supplier.application.service.FileService;
import com.xuena.supplier.infrastructure.util.EasyExcelUtil;
import com.xuena.supplier.interfaces.vo.PageVO;
import com.xuena.supplier.interfaces.vo.ResultVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/supplier")
@Tag(name = "供应商管理", description = "供应商池相关接口")
public class SupplierController {

    private final SupplierPoolService supplierPoolService;
    private final FileService fileService;

    public SupplierController(SupplierPoolService supplierPoolService, FileService fileService) {
        this.supplierPoolService = supplierPoolService;
        this.fileService = fileService;
    }

    @GetMapping("/pool")
    @Operation(summary = "查询供应商列表", description = "根据条件查询供应商池列表")
    public ResultVO<PageVO<SupplierPoolDO>> list(
            @Parameter(description = "供应商名称") @RequestParam(required = false) String name,
            @Parameter(description = "分类") @RequestParam(required = false) String category,
            @Parameter(description = "是否禁用") @RequestParam(required = false) Integer isDisabled,
            @Parameter(description = "页码") @RequestParam(defaultValue = "1") Integer pageNum,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<SupplierPoolDO> list = supplierPoolService.list(name, category, isDisabled);
        PageInfo<SupplierPoolDO> pageInfo = new PageInfo<>(list);
        return ResultVO.success(PageVO.of(list, pageInfo.getTotal(), pageNum, pageSize));
    }

    @GetMapping("/pool/{id}")
    @Operation(summary = "查询供应商详情", description = "根据ID查询供应商详情")
    public ResultVO<SupplierPoolDO> getById(
            @Parameter(description = "供应商ID") @PathVariable String id) {
        SupplierPoolDO supplier = supplierPoolService.getById(id);
        return ResultVO.success(supplier);
    }

    @PostMapping("/pool")
    @Operation(summary = "创建供应商", description = "新增供应商信息")
    public ResultVO<SupplierPoolDO> create(
            @Parameter(description = "供应商信息") @RequestBody SupplierPoolDO supplier) {
        SupplierPoolDO result = supplierPoolService.create(supplier);
        return ResultVO.success(result);
    }

    @PutMapping("/pool/{id}")
    @Operation(summary = "更新供应商", description = "根据ID更新供应商信息")
    public ResultVO<SupplierPoolDO> update(
            @Parameter(description = "供应商ID") @PathVariable String id,
            @Parameter(description = "供应商信息") @RequestBody SupplierPoolDO supplier) {
        SupplierPoolDO result = supplierPoolService.update(id, supplier);
        return ResultVO.success(result);
    }

    @DeleteMapping("/pool/{id}")
    @Operation(summary = "删除供应商", description = "根据ID删除供应商")
    public ResultVO<Void> delete(
            @Parameter(description = "供应商ID") @PathVariable String id) {
        supplierPoolService.delete(id);
        return ResultVO.success();
    }

    @DeleteMapping("/pool/batch")
    @Operation(summary = "批量删除供应商", description = "根据ID列表批量删除供应商")
    public ResultVO<Void> batchDelete(
            @Parameter(description = "供应商ID列表") @RequestBody List<String> ids) {
        supplierPoolService.batchDelete(ids);
        return ResultVO.success();
    }

    @PostMapping("/pool/{id}/enable")
    @Operation(summary = "启用供应商", description = "根据ID启用供应商")
    public ResultVO<Void> enable(
            @Parameter(description = "供应商ID") @PathVariable String id) {
        supplierPoolService.enable(id);
        return ResultVO.success();
    }

    @PostMapping("/pool/{id}/disable")
    @Operation(summary = "禁用供应商", description = "根据ID禁用供应商")
    public ResultVO<Void> disable(
            @Parameter(description = "供应商ID") @PathVariable String id) {
        supplierPoolService.disable(id);
        return ResultVO.success();
    }

    @PostMapping("/pool/import")
    @Operation(summary = "导入供应商", description = "批量导入供应商数据")
    public ResultVO<Void> importSupplier(
            @Parameter(description = "Excel文件") @RequestParam("file") MultipartFile file) throws IOException {
        List<SupplierPoolDO> suppliers = EasyExcelUtil.readExcel(file, SupplierPoolDO.class);
        for (SupplierPoolDO supplier : suppliers) {
            try {
                supplierPoolService.create(supplier);
            } catch (Exception e) {
            }
        }
        return ResultVO.success();
    }

    @GetMapping("/pool/export")
    @Operation(summary = "导出供应商", description = "按筛选条件导出供应商数据")
    public void exportSupplier(
            @Parameter(description = "供应商名称") @RequestParam(required = false) String name,
            @Parameter(description = "分类") @RequestParam(required = false) String category,
            @Parameter(description = "是否禁用") @RequestParam(required = false) Integer isDisabled,
            HttpServletResponse response) throws IOException {
        List<SupplierPoolDO> list = supplierPoolService.list(name, category, isDisabled);
        List<SupplierExportVO> exportList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            SupplierPoolDO item = list.get(i);
            SupplierExportVO vo = new SupplierExportVO();
            vo.setSerialNumber(i + 1);
            vo.setName(item.getName());
            vo.setCategory(item.getCategory());
            vo.setEnabledStatus(item.getIsDisabled() != null && item.getIsDisabled() == 1 ? "否" : "是");
            exportList.add(vo);
        }
        EasyExcelUtil.writeExcel(response, "supplier_export.xlsx", SupplierExportVO.class, exportList);
    }

    @GetMapping("/pool/export/template")
    @Operation(summary = "导出供应商模板", description = "导出供应商导入模板")
    public void exportTemplate(HttpServletResponse response) throws IOException {
        List<FileDO> templates = fileService.getTemplatesByBusinessType("SUPPLIER_POOL");
        if (!templates.isEmpty()) {
            FileDO template = templates.get(0);
            Path filePath = Paths.get(template.getFilePath());
            if (Files.exists(filePath)) {
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setCharacterEncoding("utf-8");
                String encodedFileName = URLEncoder.encode(template.getFileName(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");
                response.setHeader("Content-disposition", "attachment;filename*=UTF-8''" + encodedFileName);
                Files.copy(filePath, response.getOutputStream());
                return;
            }
        }
        EasyExcelUtil.writeExcel(response, "供应商导入模板.xlsx", SupplierTemplateVO.class, new ArrayList<>());
    }
}