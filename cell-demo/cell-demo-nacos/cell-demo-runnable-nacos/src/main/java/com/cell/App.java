//package com.cell;
//
//import com.cell.annotation.CellSpringHttpApplication;
//import com.cell.application.CellApplication;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//
///**
// * Hello world!
// */
//
////
//// 开启服务注册与发现功能
//@CellSpringHttpApplication
//@EnableDiscoveryClient
//public class App
//{
//
//
//    public static void main(String[] args)
//    {
//        System.setProperty("spring.application.name", "nacos-producer");
//        System.setProperty("spring.cloud.nacos.config.server-addr", "127.0.0.1:8848");
//        System.setProperty("spring.cloud.nacos.discovery.server-addr", "127.0.0.1:8848");
//        System.setProperty("spring.cloud.bootstrap.enabled", "false");
//
//        CellApplication.builder(App.class)
//                .newReactor()
//                .post("/post", (wp) ->
//                {
//                    wp.success("123");
//                }).make().done()
//                .build().start(args);
//    }
//}