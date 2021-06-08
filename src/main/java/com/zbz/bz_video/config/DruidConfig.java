package com.zbz.bz_video.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class DruidConfig {

    @Value("${my.druid.username:admin}")
    public String druidUserName;
    @Value("${my.druid.password:admin}")
    public String druidPassword;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource druidDataSource(){
        return new DruidDataSource();
    }

    // 后台监控
    @Bean
    public ServletRegistrationBean statViewServlet(){
        ServletRegistrationBean bean = new ServletRegistrationBean<>(new StatViewServlet(), "/druid/*");
        HashMap<String, String> initparameters = new HashMap<>();
        initparameters.put("loginUsername", druidUserName);
        initparameters.put("loginPassword", druidPassword);
        initparameters.put("allow", "");
        bean.setInitParameters(initparameters);
        return bean;
    }
}
