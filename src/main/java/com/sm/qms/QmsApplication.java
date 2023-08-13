package com.sm.qms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableAsync;

//@EnableConfigurationProperties
@EnableAsync
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class QmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(QmsApplication.class, args);
    }
}
