package com.yikster.aws.resources.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Created by TDERVILY on 03/03/2017.
 */
@ComponentScan(basePackages="com.yikster.aws.resources")
@EntityScan(basePackages="com.yikster.aws.resources.core.model")
@EnableJpaRepositories(basePackages="com.yikster.aws.resources.core.dao")
@SpringBootApplication
public class AWSResourceApplication {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(AWSResourceApplication.class, args);
    }

}
