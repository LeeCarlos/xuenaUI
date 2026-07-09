package com.xuena.supplier.controller;

import com.xuena.supplier.entity.SupplierPoolDO;
import com.xuena.supplier.service.SupplierPoolService;
import com.xuena.supplier.util.ResultVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/supplier")
public class SupplierController {

    private final SupplierPoolService supplierPoolService;

    public SupplierController(SupplierPoolService supplierPoolService) {
        this.supplierPoolService = supplierPoolService;
    }

    @GetMapping("/pool")
    public ResultVO<List<SupplierPoolDO>> list(@RequestParam(required = false) String name,
                                               @RequestParam(required = false) String category,
                                               @RequestParam(required = false) Integer isDisabled) {
        List<SupplierPoolDO> list = supplierPoolService.list(name, category, isDisabled);
        return ResultVO.success(list);
    }

    @GetMapping("/pool/{id}")
    public ResultVO<SupplierPoolDO> getById(@PathVariable Long id) {
        SupplierPoolDO supplier = supplierPoolService.getById(id);
        return ResultVO.success(supplier);
    }

    @PostMapping("/pool")
    public ResultVO<SupplierPoolDO> create(@RequestBody SupplierPoolDO supplier) {
        SupplierPoolDO result = supplierPoolService.create(supplier);
        return ResultVO.success(result);
    }

    @PutMapping("/pool/{id}")
    public ResultVO<SupplierPoolDO> update(@PathVariable Long id, @RequestBody SupplierPoolDO supplier) {
        SupplierPoolDO result = supplierPoolService.update(id, supplier);
        return ResultVO.success(result);
    }

    @DeleteMapping("/pool/{id}")
    public ResultVO<Void> delete(@PathVariable Long id) {
        supplierPoolService.delete(id);
        return ResultVO.success();
    }

    @PostMapping("/pool/{id}/enable")
    public ResultVO<Void> enable(@PathVariable Long id) {
        supplierPoolService.enable(id);
        return ResultVO.success();
    }

    @PostMapping("/pool/{id}/disable")
    public ResultVO<Void> disable(@PathVariable Long id) {
        supplierPoolService.disable(id);
        return ResultVO.success();
    }
}