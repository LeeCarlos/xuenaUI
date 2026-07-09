package com.xuena.supplier.controller;

import com.xuena.supplier.entity.CategoryDO;
import com.xuena.supplier.service.CategoryService;
import com.xuena.supplier.vo.ResultVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/category")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResultVO<List<CategoryDO>> list() {
        List<CategoryDO> list = categoryService.list();
        return ResultVO.success(list);
    }

    @GetMapping("/{id}")
    public ResultVO<CategoryDO> getById(@PathVariable Long id) {
        CategoryDO category = categoryService.getById(id);
        return ResultVO.success(category);
    }

    @PostMapping
    public ResultVO<CategoryDO> create(@RequestBody CategoryDO category) {
        CategoryDO result = categoryService.create(category);
        return ResultVO.success(result);
    }

    @PutMapping("/{id}")
    public ResultVO<CategoryDO> update(@PathVariable Long id, @RequestBody CategoryDO category) {
        CategoryDO result = categoryService.update(id, category);
        return ResultVO.success(result);
    }

    @DeleteMapping("/{id}")
    public ResultVO<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResultVO.success();
    }
}