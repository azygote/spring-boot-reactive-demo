package org.gty.demo.config;

import com.github.pagehelper.PageInterceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Objects;
import java.util.Properties;

@Configuration
public class PageHelperConfig {

    private static final Logger log = LoggerFactory.getLogger(PageHelperConfig.class);

    @Autowired
    private void init(List<SqlSessionFactory> sqlSessionFactoryList) {
        Objects.requireNonNull(sqlSessionFactoryList, "sqlSessionFactoryList must not be null");

        var pageInterceptor = new PageInterceptor();

        var properties = new Properties();
        properties.setProperty("helperDialect", "postgresql");
        properties.setProperty("offsetAsPageNum", "true");
        properties.setProperty("rowBoundsWithCount", "true");
        properties.setProperty("pageSizeZero", "true");
        properties.setProperty("reasonable", "false");
        properties.setProperty("supportMethodsArguments", "false");
        properties.setProperty("defaultCount", "true");

        pageInterceptor.setProperties(properties);

        sqlSessionFactoryList.forEach(sqlSessionFactory -> sqlSessionFactory.getConfiguration().addInterceptor(pageInterceptor));

        log.info("Mybatis PageHelper plugin initialized");
    }

}
