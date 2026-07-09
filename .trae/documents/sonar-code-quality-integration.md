# SonarQube 代码质量检查集成方案

## Context

项目当前缺少代码质量检查机制，需要集成 SonarQube 扫描规则和代码质量检查工具（Checkstyle、PMD、SpotBugs、JaCoCo），参考 SonarQube 实践，仅配置扫描规则和 Maven 插件，不部署 SonarQube 服务。目标是让 `mvn verify` 能自动运行所有质量检查，并生成覆盖率报告。

## 修改文件清单

### 修改的文件（3个）
1. `backend/pom.xml` - 添加质量检查插件配置
2. `frontend/eslint.config.js` - 增强 ESLint 规则
3. `frontend/package.json` - 添加 lint 脚本

### 新建的文件（5个）
4. `backend/quality-configs/checkstyle.xml` - Checkstyle 规则（基于阿里巴巴手册）
5. `backend/quality-configs/pmd-ruleset.xml` - PMD 自定义规则集
6. `backend/quality-configs/spotbugs-exclude.xml` - SpotBugs 排除规则（含 Lombok 排除）
7. `backend/sonar-project.properties` - 后端 SonarQube 项目配置
8. `frontend/sonar-project.properties` - 前端 SonarQube 配置

## 实施步骤

### 步骤1：修改 backend/pom.xml

**新增 properties（版本管理）：**
- sonar.version: 4.0.0.4121
- checkstyle.plugin.version: 3.6.0 / checkstyle.version: 10.17.1
- pmd.plugin.version: 3.26.0
- spotbugs.plugin.version: 4.8.6.4 / spotbugs.version: 4.8.6
- jacoco.version: 0.8.12
- jacoco.minimum.line.coverage: 0.30
- jacoco.minimum.branch.coverage: 0.20

**新增 dependency：**
- `com.github.spotbugs:spotbugs-annotations:4.8.6` (scope=provided，支持 @SuppressFBWarnings)

**新增 5 个 build plugins（绑定到 verify 阶段）：**

1. **jacoco-maven-plugin** - 三个 execution：
   - `prepare-agent`：测试前注入 agent
   - `report`：verify 阶段生成 XML/HTML 报告
   - `check`：verify 阶段验证覆盖率（LINE≥30%, BRANCH≥20%）
   - 排除 entity/config/enums/vo/SupplierApplication/GeneratePassword

2. **maven-checkstyle-plugin** - 配置：
   - configLocation: quality-configs/checkstyle.xml
   - failsOnError=true, failOnViolation=true
   - violationSeverity=warning
   - includeTestSourceDirectory=true

3. **maven-pmd-plugin** - 配置：
   - rulesets: quality-configs/pmd-ruleset.xml
   - failOnViolation=true, targetJdk=17
   - analysisCache=true（增量分析）

4. **spotbugs-maven-plugin** - 配置：
   - effort=More, threshold=Normal
   - excludeFilterFile: quality-configs/spotbugs-exclude.xml
   - 集成 findsecbugs-plugin 1.13.0（安全漏洞检测）

5. **sonar-maven-plugin** - 不绑定阶段，手动 `mvn sonar:sonar` 调用

**新增 profiles：**
- `quick` profile：跳过所有质量检查（checkstyle.skip/pmd.skip/spotbugs.skip/jacoco.skip）

### 步骤2：创建 backend/quality-configs/checkstyle.xml

基于阿里巴巴 Java 开发手册的 Checkstyle 配置（DTD 1.3），关键规则：
- **命名规范**：TypeName(PascalCase)、MethodName(camelCase)、ConstantName(UPPER_SNAKE)、PackageName(全小写)
- **Import 规范**：AvoidStarImport、UnusedImports、ImportOrder
- **代码格式**：LineLength(120)、Indentation(4空格)、NeedBraces、OneStatementPerLine
- **编码规范**：EqualsHashCode、StringLiteralEquality、NestedIfDepth(max=3)、MethodLength(max=150)、ParameterNumber(max=7)
- **注释规范**：MissingJavadocType(仅public类)、JavadocStyle
- 支持 SuppressionCommentFilter（`//CHECKSTYLE:OFF` 跳过检查）

### 步骤3：创建 backend/quality-configs/pmd-ruleset.xml

PMD 7.x category-based 规则集，精选规则：
- **Best Practices**：UnusedFormalParameter、UnusedLocalVariable、AvoidPrintStackTrace、GuardLogStatement
- **Code Style**：EmptyControlStatement、UnnecessaryFullyQualifiedName、UselessParentheses、LocalVariableCouldBeFinal
- **Design**：CollapsibleIfStatements、GodClass、CyclomaticComplexity(max=15)、ExcessiveMethodLength(max=150)
- **Error Prone**：BrokenNullCheck、OverrideBothEqualsAndHashcode、ReturnFromFinallyBlock、AvoidDecimalLiteralsInBigDecimalConstructor
- **Performance**：AppendCharacterWithChar、InefficientStringBuffering、UseStringBufferForStringAppends
- 排除 BeanMembersShouldSerialize（项目用 Lombok @Data，不用 Java 序列化）

### 步骤4：创建 backend/quality-configs/spotbugs-exclude.xml

排除 Lombok 和项目特定误报：
- entity 包下所有类的 EI_EXPOSE_REP/EI_EXPOSE_REP2（Lombok @Data 生成 Date getter/setter）
- vo 包下所有类的 EI_EXPOSE_REP/EI_EXPOSE_REP2
- GeneratePassword 工具类全部排除
- config 包的 RCN_REDUNDANT_NULLCHECK_OF_NONNULL_VALUE（Spring 注入误报）

### 步骤5：创建 backend/sonar-project.properties

```properties
sonar.projectKey=com.xuena:supplier-performance
sonar.projectName=供应商绩效分析系统
sonar.sources=src/main/java
sonar.tests=src/test/java
sonar.java.binaries=target/classes
sonar.java.coveragePlugin=jacoco
sonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml
sonar.coverage.exclusions=**/entity/**,**/config/**,**/enums/**,**/vo/**,**/SupplierApplication.java
sonar.java.source=17
```

### 步骤6：修改 frontend/eslint.config.js

增强规则（保持实用）：
- 逻辑类规则设为 error：eqeqeq、no-eval、no-implied-eval、no-throw-literal、prefer-const、no-var
- 风格类规则设为 warn：no-console、prefer-template、object-shorthand、arrow-body-style
- 保留现有 react-hooks 和 react-refresh 规则
- ignores 增加 coverage 目录

### 步骤7：修改 frontend/package.json

新增 scripts：
- `lint:fix`: `eslint . --fix`
- `lint:report`: `eslint . -f json -o eslint-report.json`

### 步骤8：创建 frontend/sonar-project.properties

```properties
sonar.projectKey=com.xuena:supplier-performance-frontend
sonar.sources=src
sonar.exclusions=**/node_modules/**,**/dist/**,**/*.css
sonar.eslint.reportPaths=eslint-report.json
```

## 执行命令

```bash
# 后端完整质量检查
cd backend && mvn clean verify

# 后端快速构建（跳过质量检查）
mvn clean verify -Pquick

# 单独运行各工具
mvn checkstyle:check    # Checkstyle
mvn pmd:check           # PMD
mvn spotbugs:check      # SpotBugs
mvn test jacoco:report  # JaCoCo 覆盖率

# 前端代码检查
cd frontend && npm run lint
npm run lint:fix        # 自动修复
npm run lint:report     # 生成 JSON 报告
```

## 报告输出位置

| 工具 | 路径 | 格式 |
|---|---|---|
| Checkstyle | backend/target/checkstyle-result.xml | XML |
| PMD | backend/target/pmd.xml | XML |
| SpotBugs | backend/target/spotbugsXml.xml | XML |
| JaCoCo | backend/target/site/jacoco/index.html | HTML |
| ESLint | frontend/eslint-report.json | JSON |

## 验证步骤

1. **验证插件配置**：`mvn clean verify -Dcheckstyle.skip=true -Dpmd.skip=true -Dspotbugs.skip=true -Djacoco.skip=true` 应成功
2. **验证 JaCoCo**：`mvn clean test jacoco:report`，打开 `target/site/jacoco/index.html` 查看覆盖率
3. **逐步启用各工具**：单独运行 checkstyle/pmd/spotbugs，修复违规代码
4. **完整运行**：`mvn clean verify` 应成功（可能需要先修复代码违规）
5. **前端验证**：`cd frontend && npm run lint`，检查并修复违规

## 注意事项

- 首次运行 `mvn verify` 预期会有代码违规（如 AuthServiceImpl 中的全限定名、空方法等），需要逐步修复
- JaCoCo 覆盖率阈值初始设为 30%/20%（较保守），如失败可先用 `-Djacoco.skip=true` 跳过，待补充测试后再启用
- 使用 `-Pquick` profile 可快速构建跳过所有检查
- SpotBugs 的 entity 包排除是关键，否则 Lombok @Data 生成的 Date getter/setter 会产生大量误报
