package com.magictan.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusProperties;
import com.baomidou.mybatisplus.core.config.GlobalConfig;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @author: magicTan
 * @time: 2024/6/11
 */
@Configuration
@MapperScan(basePackages = "com.magictan.mapper", sqlSessionFactoryRef = "customSessionFactory")
public class MybatisConfig {

//    @Bean
//    public SqlSessionFactory customSessionFactory(DataSource dataSource) throws Exception {
//        MybatisSqlSessionFactoryBean sessionFactoryBean = new MybatisSqlSessionFactoryBean();
//        sessionFactoryBean.setDataSource(dataSource);
//        return sessionFactoryBean.getObject();
//    }

    @Autowired
    private MybatisPlusProperties mybatisPlusProperties;

    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
        mybatisPlusInterceptor.addInnerInterceptor(new PaginationInnerInterceptor());
        return mybatisPlusInterceptor;
    }

    @Bean
    public SqlSessionFactory customSessionFactory(DataSource dataSource,
                                                  MybatisPlusInterceptor mybatisPlusInterceptor,
                                                  TestMetaObjectHandler testMetaObjectHandler) throws Exception {
        MybatisSqlSessionFactoryBean sessionFactoryBean = new MybatisSqlSessionFactoryBean();
        sessionFactoryBean.setDataSource(dataSource);
        sessionFactoryBean.setPlugins(mybatisPlusInterceptor);

        GlobalConfig globalConfig = this.mybatisPlusProperties.getGlobalConfig();
        globalConfig.setMetaObjectHandler(testMetaObjectHandler);
        sessionFactoryBean.setGlobalConfig(globalConfig);

        return sessionFactoryBean.getObject();
    }
}
