package com.xuena.supplier.config;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class EasyExcelConfig {

    @Bean
    public <T> AnalysisEventListener<T> excelListener() {
        return new AnalysisEventListener<T>() {
            private final List<T> dataList = new ArrayList<>();

            @Override
            public void invoke(T data, AnalysisContext context) {
                dataList.add(data);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
            }

            public List<T> getDataList() {
                return dataList;
            }
        };
    }
}