package com.mojiayi.action.dubbo.provider.order.datasource;

import com.alibaba.druid.pool.DruidDataSource;
import com.mojiayi.action.dubbo.provider.order.dao.BaseMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.sql.SQLException;

/**
 * @author mojiayi
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(DruidConfig.class)
@EnableTransactionManagement
@MapperScan(basePackages = {"com.mojiayi.action.dubbo.provider.order.dao.**"}, sqlSessionTemplateRef = "mojiayiSqlSessionTemplate", markerInterface = BaseMapper.class)
public class MojiayiDataSource {
    @Value("${spring.datasource.db.url}")
    private String dbUrl;

    @Value("${spring.datasource.db.username}")
    private String username;

    @Value("${spring.datasource.db.password}")
    private String password;

    @Value("${spring.datasource.db.driver-class-name}")
    private String driverClassName;

    @Autowired
    private DruidConfig druidConfig;

    @Bean
    public javax.sql.DataSource mojiayiDatasource() throws SQLException {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setName("datasource-mysql");
        datasource.setUrl(dbUrl);
        datasource.setUsername(username);
        datasource.setPassword(password);
        datasource.setDriverClassName(driverClassName);
        BeanUtils.copyProperties(druidConfig, datasource);
        datasource.init();
        return datasource;
    }

    @Bean(name = "mojiayiSqlSessionFactory")
    public SqlSessionFactory mojiayiSqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(mojiayiDatasource());
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:mapper/*.xml"));
        return bean.getObject();
    }

    @Bean(name = "mojiayiTransactionManager")
    public DataSourceTransactionManager mojiayiTransactionManager() throws SQLException {
        return new DataSourceTransactionManager(mojiayiDatasource());
    }

    @Bean(name = "mojiayiSqlSessionTemplate")
    public SqlSessionTemplate mojiayiSqlSessionTemplate(@Qualifier("mojiayiSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
