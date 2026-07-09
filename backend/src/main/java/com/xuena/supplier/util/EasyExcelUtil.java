package com.xuena.supplier.util;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class EasyExcelUtil {

    public static <T> List<T> readExcel(MultipartFile file, Class<T> clazz) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            return EasyExcel.read(inputStream, clazz, null).doReadSync();
        }
    }

    public static <T> List<T> readExcel(MultipartFile file, Class<T> clazz, int headRowNumber) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            return EasyExcel.read(inputStream, clazz, null)
                    .headRowNumber(headRowNumber)
                    .doReadSync();
        }
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
}