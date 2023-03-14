package com.mojiayi.action.excel.core.responsibilitychain;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.base.CaseFormat;
import com.mojiayi.action.excel.annotation.FixedColumn;
import com.mojiayi.action.excel.core.ExcelImportTool;
import com.mojiayi.action.excel.domain.BaseEntity;
import com.mojiayi.action.excel.dto.ExcelTemplateDTO;
import com.mojiayi.action.excel.dto.FixedTableHeader;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 导入数据校验和保存入库的责任链
 * </p>
 *
 * @author mojiayi
 */
@Slf4j
public abstract class ExcelImportResponsibilityChainAbstractHandler {
    private ExcelImportResponsibilityChainAbstractHandler nextHandler;

    protected final Map<String, Class<?>> extBasicDataClassMap = new ConcurrentHashMap<>();

    @Resource
    private ExcelImportTool excelImportTool;

    @Value("${excel-domain-package:}")
    private String excelDomainPackage;

    /**
     * 构建表名和对应domain类的映射关系
     *
     * @throws ClassNotFoundException 类不存在的异常
     */
    @PostConstruct
    public void init() throws ClassNotFoundException {
        if (Strings.isEmpty(excelDomainPackage)) {
            log.error("未指定excel导入数据的domain类包路径");
            System.exit(1);
        }
        log.error("excel导入数据的domain类包路径={}", excelDomainPackage);
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(BaseEntity.class));

        Set<BeanDefinition> components = provider.findCandidateComponents(excelDomainPackage);

        TableName tableNameAnnotation = null;
        for (BeanDefinition component : components) {
            Class<?> clazz = Class.forName(component.getBeanClassName());
            tableNameAnnotation = clazz.getAnnotation(TableName.class);
            if (tableNameAnnotation == null) {
                continue;
            }
            extBasicDataClassMap.put(tableNameAnnotation.value(), clazz);
        }
    }

    abstract String doFilter(String tableName, List<String> headerNameList, List<List<String>> dataRowList, HttpServletResponse response);

    public String filter(String tableName, List<String> headerNameList, List<List<String>> dataRowList, HttpServletResponse response) {
        String errMsg = doFilter(tableName, headerNameList, dataRowList, response);
        if (Strings.isNotEmpty(errMsg)) {
            return errMsg;
        }
        if (this.getNextHandler() != null) {
            return this.getNextHandler().filter(tableName, headerNameList, dataRowList, response);
        }
        return null;
    }

    public ExcelImportResponsibilityChainAbstractHandler getNextHandler() {
        return nextHandler;
    }

    public void setNextHandler(ExcelImportResponsibilityChainAbstractHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    /**
     * 根据domain对象类上的注解，提取组成唯一索引的字段
     *
     * @param tableName 表名
     * @param dataClass domain对象类
     * @return 返回组成唯一索引的字段
     */
    protected List<String> getUniqueKeyFieldList(String tableName, Class<?> dataClass) {
        if (dataClass == null) {
            log.error("表{}没有对应的Domain类定义", tableName);
            return Collections.emptyList();
        }

        List<String> uniqueKeyFieldNameList = new ArrayList<>();
        FixedColumn annotation = null;
        for (Field field : dataClass.getDeclaredFields()) {
            annotation = field.getAnnotation(FixedColumn.class);
            if (annotation == null) {
                continue;
            }
            if (annotation.uniqueKeyFlag()) {
                uniqueKeyFieldNameList.add(field.getName());
            }
        }
        return uniqueKeyFieldNameList;
    }

    /**
     * 根据表名获取数据库表中字段注释(key)和字段名(value)的映射关系，其中value是字段名从下划线格式转换成了驼峰命名格式，比如 customer_code 变成 customerCode
     *
     * @param tableName 表名
     * @return 返回数据库表中字段注释和字段名的映射关系
     */
    protected Map<String, String> getFieldNameAndPropMap(String tableName) {
        ExcelTemplateDTO excelTemplateDTO = excelImportTool.getTemplateHeader(tableName);
        Map<String, String> fieldNameAndPropMap = new HashMap<>();
        for (FixedTableHeader tableHeader : excelTemplateDTO.getHeaderList()) {
            fieldNameAndPropMap.put(tableHeader.getName(), CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, tableHeader.getProp()));
            if (CollUtil.isNotEmpty(tableHeader.getChildren())) {
                for (FixedTableHeader childHeader : tableHeader.getChildren()) {
                    fieldNameAndPropMap.put(childHeader.getName(), CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, childHeader.getProp()));
                }
            }
        }
        return fieldNameAndPropMap;
    }
}
