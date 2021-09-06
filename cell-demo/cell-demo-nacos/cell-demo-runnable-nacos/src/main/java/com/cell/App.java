package com.cell;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * Hello world!
 */

//
//// 开启服务注册与发现功能
//@CellSpringHttpApplication
//@EnableDiscoveryClient
//public class App
//{
////    @Primary
////    @Bean
////    public NacosConfigProperties properties()
////    {
////        NacosConfigProperties properties2 = new NacosConfigProperties();
////        properties2.setServerAddr("127.0.0.1:8888");
////        return properties2;
////    }
//
//    public static void main(String[] args)
//    {
//        Properties properties = new Properties();
//        NacosConfigProperties properties2 = new NacosConfigProperties();
//        System.setProperty("spring.cloud.nacos.config.server-addr", "127.0.0.1:8888");
//        System.setProperty("spring.cloud.nacos.discovery.server-addr", "127.0.0.1:8888");
////        System.setProperty("spring.cloud.bootstrap.enabled", "false");
////        System.setProperty("nacos.config.serverAddr", "");
//        CellApplication.builder(App.class).properties("spring.cloud.nacos.config.server-addr=127.0.0.1:8888")
//                .build().start(args);
//    }
//}

@SpringBootApplication
@EnableDiscoveryClient
public class App
{

    public static void main(String[] args)
    {
        SpringApplication.run(App.class, args);
    }

    @RestController
    public class EchoController
    {
        @GetMapping(value = "/echo/{string}")
        public String echo(@PathVariable String string)
        {
            return "Hello Nacos Discovery " + string;
        }
    }
}