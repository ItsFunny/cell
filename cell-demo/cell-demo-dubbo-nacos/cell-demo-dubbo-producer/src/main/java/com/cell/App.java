package com.cell;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 */
@EnableDubbo
@SpringBootApplication
public class App
{
    public static void main(String[] args)
    {
//        System.setProperty("server.port", "14511");
//        System.setProperty("spring.application.name", "provider-ervice");
//        System.setProperty("dubbo.application.id", "provider-service");
//        System.setProperty("dubbo.application.name", "provider-service");
//        System.setProperty("dubbo.registry.address", "nacos://127.0.0.1:8848");
//        System.setProperty("dubbo.registry.username", "nacos");
//        System.setProperty("dubbo.registry.password", "nacos");
//        System.setProperty("dubbo.registry.id", "proasd");
//        System.setProperty("dubbo.scan.base-packages", "com.cell.services.impl");
//        System.setProperty("dubbo.protocol.name", "dubbo");
//        System.setProperty("dubbo.protocol.port", "15511");
//        System.setProperty("dubbo.protocol.id", "dubbo");
        SpringApplication.run(App.class, args);
    }
}
