package com.xuena.supplier.interfaces.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SupplierExportVO {

    @ExcelProperty("序号")
    private Integer serialNumber;

    @ExcelProperty("供应商名称")
    private String name;

    @ExcelProperty("品类")
    private String category;

    @ExcelProperty("启用状态")
    private String enabledStatus;
}