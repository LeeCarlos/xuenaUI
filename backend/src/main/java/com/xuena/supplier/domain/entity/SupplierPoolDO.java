package com.xuena.supplier.domain.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

@Data
public class SupplierPoolDO {

    @ExcelProperty("序号")
    private String id;
    @ExcelProperty("供应商名称")
    private String name;
    @ExcelProperty("类别")
    private String category;
    @ExcelProperty("是否禁用")
    private Integer isDisabled;
    private Integer isDeleted;
    @ExcelProperty("创建人")
    private String createName;
    @ExcelProperty("创建人ID")
    private String createId;
    @ExcelProperty("创建日期")
    private Date createDate;
    @ExcelProperty("更新人")
    private String updateName;
    @ExcelProperty("更新人ID")
    private String updateId;
    @ExcelProperty("更新时间")
    private Date updateDate;
}
