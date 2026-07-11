package com.xuena.supplier.interfaces.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

@Data
public class SupplierTemplateVO {

    @ExcelProperty("序号")
    private String id;

    @ExcelProperty("供应商名称")
    private String name;

    @ExcelProperty("类别")
    private String category;
}