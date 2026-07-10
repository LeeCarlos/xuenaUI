package com.xuena.supplier.interfaces.controller;

import com.xuena.supplier.domain.entity.CategoryDO;
import com.xuena.supplier.application.service.CategoryService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@Tag(name = "分类管理", description = "供应商分类相关接口")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @Operation(summary = "查询分类列表", description = "查询所有分类信息")
    public ResultVO<List<CategoryDO>> list() {
        List<CategoryDO> list = categoryService.list();
        return ResultVO.success(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询分类详情", description = "根据ID查询分类详情")
    public ResultVO<CategoryDO> getById(
            @Parameter(description = "分类ID") @PathVariable String id) {
        CategoryDO category = categoryService.getById(id);
        return ResultVO.success(category);
    }

    @PostMapping
    @Operation(summary = "创建分类", description = "新增分类信息")
    public ResultVO<CategoryDO> create(
            @Parameter(description = "分类信息") @RequestBody CategoryDO category) {
        CategoryDO result = categoryService.create(category);
        return ResultVO.success(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新分类", description = "根据ID更新分类信息")
    public ResultVO<CategoryDO> update(
            @Parameter(description = "分类ID") @PathVariable String id,
            @Parameter(description = "分类信息") @RequestBody CategoryDO category) {
        CategoryDO result = categoryService.update(id, category);
        return ResultVO.success(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除分类", description = "根据ID删除分类")
    public ResultVO<Void> delete(
            @Parameter(description = "分类ID") @PathVariable String id) {
        categoryService.delete(id);
        return ResultVO.success();
    }
}