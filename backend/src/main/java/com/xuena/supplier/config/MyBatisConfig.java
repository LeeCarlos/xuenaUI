package com.xuena.supplier.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.xuena.supplier.mapper")
public class MyBatisConfig {
}