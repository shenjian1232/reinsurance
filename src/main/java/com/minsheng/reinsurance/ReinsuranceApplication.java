package com.minsheng.reinsurance;

import com.minsheng.reinsurance.config.AdeviceConfig;
import com.minsheng.reinsurance.config.CurrencyConfig;
import com.minsheng.reinsurance.config.MailConfig;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.io.File;
//
//@SpringBootApplication
//@MapperScan("com.mingsheng.reinsurance.dao")

//@SpringBootApplication
//@MapperScan("com.mingsheng.reinsurance.dao")
//@EnableAsync
//@EnableTransactionManagement
//@EnableConfigurationProperties({CurrencyConfig.class, MailConfig.class, AdeviceConfig.class})

/**
 *
 */
@SpringBootApplication
@MapperScan("com.minsheng.reinsurance.dao")
public class ReinsuranceApplication   {


    public static void main(String[] args) {

        String path =  System.getProperty("user.dir") + File.separator+"/src/main/resources";
        System.setProperty("webroot",path);
        SpringApplication.run(ReinsuranceApplication.class, args);
    }


}

