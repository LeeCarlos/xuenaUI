package com.xuena.supplier.interfaces.controller;

import com.xuena.supplier.domain.entity.SupplierPoolDO;
import com.xuena.supplier.application.service.SupplierPoolService;
import com.xuena.supplier.interfaces.vo.ResultVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/supplier")
@Tag(name = "供应商管理", description = "供应商池相关接口")
public class SupplierController {

    private final SupplierPoolService supplierPoolService;

    public SupplierController(SupplierPoolService supplierPoolService) {
        this.supplierPoolService = supplierPoolService;
    }

    @GetMapping("/pool")
    @Operation(summary = "查询供应商列表", description = "根据条件查询供应商池列表")
    public ResultVO<List<SupplierPoolDO>> list(
            @Parameter(description = "供应商名称") @RequestParam(required = false) String name,
            @Parameter(description = "分类") @RequestParam(required = false) String category,
            @Parameter(description = "是否禁用") @RequestParam(required = false) Integer isDisabled) {
        List<SupplierPoolDO> list = supplierPoolService.list(name, category, isDisabled);
        return ResultVO.success(list);
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
}