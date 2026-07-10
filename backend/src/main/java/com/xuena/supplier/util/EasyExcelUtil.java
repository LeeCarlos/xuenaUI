package com.xuena.supplier.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class EasyExcelUtil {

    public static <T> List<T> readExcel(MultipartFile file, Class<T> clazz) throws IOException {
        List<T> data = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream()) {
            EasyExcel.read(inputStream, clazz, new AnalysisEventListener<T>() {
                @Override
                public void invoke(T t, AnalysisContext analysisContext) {
                    data.add(t);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                }
            }).sheet().doRead();
        }
        return data;
    }

    public static <T> List<T> readExcel(MultipartFile file, Class<T> clazz, int headRowNumber) throws IOException {
        List<T> data = new ArrayList<>();
        try (InputStream inputStream = file.getInputStream()) {
            EasyExcel.read(inputStream, clazz, new AnalysisEventListener<T>() {
                @Override
                public void invoke(T t, AnalysisContext analysisContext) {
                    data.add(t);
                }

                @Override
                public void doAfterAllAnalysed(AnalysisContext analysisContext) {
                }
            }).headRowNumber(headRowNumber).sheet().doRead();
        }
        return data;
    }

    public static <T> byte[] writeExcel(List<T> data, Class<T> clazz, String sheetName) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            EasyExcel.write(outputStream, clazz)
                    .sheet(sheetName)
                    .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                    .doWrite(data);
            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void writeExcel(HttpServletResponse response, String fileName,
            Class<T> clazz, List<T> data) throws IOException {
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=UTF-8''" + encodedFileName);
        EasyExcel.write(response.getOutputStream(), clazz)
                .sheet("Sheet1")
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .doWrite(data);
    }
}