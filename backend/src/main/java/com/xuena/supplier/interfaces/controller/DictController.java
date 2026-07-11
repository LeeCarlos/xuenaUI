package com.xuena.supplier.interfaces.controller;

import com.xuena.supplier.domain.entity.DictDO;
import com.xuena.supplier.application.service.DictService;
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
@RequestMapping("/api/dict")
@Tag(name = "字典管理", description = "字典相关接口")
public class DictController {

    private final DictService dictService;

    public DictController(DictService dictService) {
        this.dictService = dictService;
    }

    @GetMapping
    @Operation(summary = "查询字典列表", description = "查询所有字典或按字典项查询")
    public ResultVO<List<DictDO>> list(
            @Parameter(description = "字典项") @RequestParam(value = "item", required = false) String item) {
        List<DictDO> list;
        if (item != null && !item.isEmpty()) {
            list = dictService.listByItem(item);
        } else {
            list = dictService.listAll();
        }
        return ResultVO.success(list);
    }

    @GetMapping("/{id}")
    @Operation(summary = "查询字典详情", description = "根据ID查询字典详情")
    public ResultVO<DictDO> getById(
            @Parameter(description = "字典ID") @PathVariable String id) {
        DictDO dict = dictService.getById(id);
        return ResultVO.success(dict);
    }

    @PostMapping
    @Operation(summary = "创建字典", description = "新增字典信息")
    public ResultVO<DictDO> create(
            @Parameter(description = "字典信息") @RequestBody DictDO dict) {
        DictDO result = dictService.create(dict);
        return ResultVO.success(result);
    }

    @PutMapping("/{id}")
    @Operation(summary = "更新字典", description = "根据ID更新字典信息")
    public ResultVO<DictDO> update(
            @Parameter(description = "字典ID") @PathVariable String id,
            @Parameter(description = "字典信息") @RequestBody DictDO dict) {
        DictDO result = dictService.update(id, dict);
        return ResultVO.success(result);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除字典", description = "根据ID删除字典")
    public ResultVO<Void> delete(
            @Parameter(description = "字典ID") @PathVariable String id) {
        dictService.delete(id);
        return ResultVO.success();
    }

    @PostMapping("/sync-category")
    @Operation(summary = "同步供应商类别", description = "从供应商列表中提取去重后的类别，维护到字典表中")
    public ResultVO<Integer> syncCategory() {
        int count = dictService.syncCategoryFromSupplier();
        return ResultVO.success(count);
    }
}