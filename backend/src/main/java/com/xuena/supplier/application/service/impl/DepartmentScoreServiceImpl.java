package com.xuena.supplier.application.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.write.style.column.LongestMatchColumnWidthStyleStrategy;
import com.xuena.supplier.domain.entity.DepartmentScoreDO;
import com.xuena.supplier.domain.entity.FileDO;
import com.xuena.supplier.domain.exception.BusinessException;
import com.xuena.supplier.infrastructure.config.DepartmentTemplateConfig;
import com.xuena.supplier.infrastructure.config.DepartmentTemplateConfig.Dimension;
import com.xuena.supplier.infrastructure.mapper.DepartmentScoreMapper;
import com.xuena.supplier.application.service.DepartmentScoreService;
import com.xuena.supplier.application.service.FileService;
import com.xuena.supplier.infrastructure.util.IdGenerator;
import com.xuena.supplier.infrastructure.util.UserContext;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class DepartmentScoreServiceImpl implements DepartmentScoreService {

    private final DepartmentScoreMapper departmentScoreMapper;
    private final IdGenerator idGenerator;
    private final FileService fileService;

    public DepartmentScoreServiceImpl(DepartmentScoreMapper departmentScoreMapper, IdGenerator idGenerator, FileService fileService) {
        this.departmentScoreMapper = departmentScoreMapper;
        this.idGenerator = idGenerator;
        this.fileService = fileService;
    }

    @Override
    public DepartmentScoreDO getById(String id) {
        DepartmentScoreDO score = departmentScoreMapper.selectById(id);
        if (score == null) {
            throw new BusinessException("部门打分记录不存在");
        }
        return score;
    }

    @Override
    public List<DepartmentScoreDO> list(String yearMonth, String supplierName, String department) {
        return departmentScoreMapper.selectList(yearMonth, supplierName, department);
    }

    @Override
    @Transactional
    public DepartmentScoreDO create(DepartmentScoreDO score) {
        score.setId(idGenerator.generateId());
        if (score.getStatus() == null) {
            score.setStatus("IN_PROGRESS");
        }
        score.setCreateId(UserContext.getUserId());
        score.setCreateName(UserContext.getUserName());
        departmentScoreMapper.insert(score);
        return score;
    }

    @Override
    @Transactional
    public DepartmentScoreDO update(String id, DepartmentScoreDO score) {
        DepartmentScoreDO existing = getById(id);
        if ("COMPLETED".equals(existing.getStatus())) {
            throw new BusinessException("已完成的打分记录不能修改");
        }
        score.setId(id);
        score.setUpdateId(UserContext.getUserId());
        score.setUpdateName(UserContext.getUserName());
        departmentScoreMapper.update(score);
        return score;
    }

    @Override
    @Transactional
    public void delete(String id) {
        DepartmentScoreDO score = getById(id);
        if ("COMPLETED".equals(score.getStatus())) {
            throw new BusinessException("已完成的打分记录不能删除");
        }
        departmentScoreMapper.delete(id);
    }

    @Override
    @Transactional
    public void submit(String id) {
        DepartmentScoreDO score = getById(id);
        if ("COMPLETED".equals(score.getStatus())) {
            throw new BusinessException("已完成的打分记录不能提交");
        }
        departmentScoreMapper.updateStatus(id, "IN_PROGRESS");
    }

    @Override
    @Transactional
    public void complete(String id) {
        DepartmentScoreDO score = getById(id);
        if ("COMPLETED".equals(score.getStatus())) {
            throw new BusinessException("打分记录已完成");
        }
        departmentScoreMapper.updateStatus(id, "COMPLETED");
    }

    @Override
    public void exportTemplate(String department, HttpServletResponse response) throws IOException {
        List<FileDO> templates = fileService.getTemplatesByBusinessType("DEPARTMENT_SCORE");
        if (!templates.isEmpty()) {
            FileDO template = templates.get(0);
            Path filePath = Paths.get(template.getFilePath());
            if (Files.exists(filePath)) {
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setCharacterEncoding("utf-8");
                String encodedFileName = URLEncoder.encode(template.getFileName(), StandardCharsets.UTF_8).replaceAll("\\+", "%20");
                response.setHeader("Content-disposition", "attachment;filename*=UTF-8''" + encodedFileName);
                Files.copy(filePath, response.getOutputStream());
                return;
            }
        }

        List<Dimension> dimensions = DepartmentTemplateConfig.getDimensions(department);
        String exceptionColumn = DepartmentTemplateConfig.getExceptionColumn(department);

        List<List<String>> head = new ArrayList<>();
        head.add(List.of("", "", ""));
        head.add(List.of("", "", ""));
        head.add(List.of("", "", department));

        List<String> headerRow = new ArrayList<>();
        headerRow.add("序号");
        headerRow.add("供应商名称");
        headerRow.add("品类");
        for (Dimension dim : dimensions) {
            headerRow.add(dim.getName());
        }
        headerRow.add(exceptionColumn);
        head.add(headerRow);

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String encodedFileName = URLEncoder.encode(department + "打分导入模板.xlsx", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=UTF-8''" + encodedFileName);

        EasyExcel.write(response.getOutputStream())
                .registerWriteHandler(new LongestMatchColumnWidthStyleStrategy())
                .sheet("Sheet1")
                .head(head)
                .doWrite(new ArrayList<>());
    }

    @Override
    @Transactional
    public void batchImport(MultipartFile file, String department) throws IOException {
        List<Dimension> dimensions = DepartmentTemplateConfig.getDimensions(department);
        String exceptionColumn = DepartmentTemplateConfig.getExceptionColumn(department);

        List<DepartmentScoreDO> scoreList = new ArrayList<>();

        EasyExcel.read(file.getInputStream(), new AnalysisEventListener<Map<Integer, Object>>() {
            @Override
            public void invoke(Map<Integer, Object> rowData, AnalysisContext context) {
                int rowNum = context.readRowHolder().getRowIndex();
                if (rowNum <= 3) {
                    return;
                }

                Object seqObj = rowData.get(0);
                if (seqObj == null || seqObj.toString().trim().isEmpty()) {
                    return;
                }

                String supplierName = getCellValue(rowData.get(1));
                String category = getCellValue(rowData.get(2));

                if (supplierName == null || supplierName.trim().isEmpty()) {
                    return;
                }

                for (int i = 0; i < dimensions.size(); i++) {
                    Dimension dim = dimensions.get(i);
                    Object scoreObj = rowData.get(3 + i);
                    String dimensionScore = scoreObj != null ? scoreObj.toString().trim() : "";

                    DepartmentScoreDO score = new DepartmentScoreDO();
                    score.setId(idGenerator.generateId());
                    score.setYearMonth(getCurrentYearMonth());
                    score.setSupplierName(supplierName);
                    score.setDepartment(department);
                    score.setDimensionGroup(dim.getCode());
                    if (!dimensionScore.isEmpty()) {
                        score.setDimensionScore(new BigDecimal(dimensionScore));
                    }
                    score.setSubScores(null);
                    score.setStatus("IN_PROGRESS");
                    score.setFileName(file.getOriginalFilename());
                    score.setCreateId(UserContext.getUserId());
                    score.setCreateName(UserContext.getUserName());
                    scoreList.add(score);
                }

                Object exceptionObj = rowData.get(3 + dimensions.size());
                String exceptionReason = getCellValue(exceptionObj);
                if (exceptionReason != null && !exceptionReason.trim().isEmpty()) {
                    for (Dimension dim : dimensions) {
                        DepartmentScoreDO exceptionScore = new DepartmentScoreDO();
                        exceptionScore.setId(idGenerator.generateId());
                        exceptionScore.setYearMonth(getCurrentYearMonth());
                        exceptionScore.setSupplierName(supplierName);
                        exceptionScore.setDepartment(department);
                        exceptionScore.setDimensionGroup(dim.getCode());
                        exceptionScore.setDimensionScore(null);
                        exceptionScore.setSubScores(null);
                        exceptionScore.setExceptionReason(exceptionReason);
                        exceptionScore.setStatus("IN_PROGRESS");
                        exceptionScore.setFileName(file.getOriginalFilename());
                        exceptionScore.setCreateId(UserContext.getUserId());
                        exceptionScore.setCreateName(UserContext.getUserName());
                        scoreList.add(exceptionScore);
                    }
                }
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
            }
        }).headRowNumber(4).sheet().doRead();

        if (!scoreList.isEmpty()) {
            departmentScoreMapper.batchInsert(scoreList);
        }
    }

    private String getCellValue(Object obj) {
        if (obj == null) {
            return null;
        }
        String value = obj.toString().trim();
        return value.isEmpty() ? null : value;
    }

    private String getCurrentYearMonth() {
        java.time.LocalDate now = java.time.LocalDate.now();
        java.time.LocalDate lastMonth = now.minusMonths(1);
        return lastMonth.toString().substring(0, 7);
    }
}