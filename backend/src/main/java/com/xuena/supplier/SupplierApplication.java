package com.xuena.supplier;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.xuena.supplier.infrastructure.mapper")
public class SupplierApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(SupplierApplication.class, args);
    }
}