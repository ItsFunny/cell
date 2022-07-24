package com.cell.component.http.swagger.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.ApiSelectorBuilder;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: amber
 * Date: 2018-05-16
 * Time: 16:17
 */
@Configuration
@EnableSwagger2
@Order(Integer.MAX_VALUE - 1)
public class SwaggerConfiguration
{

    @Bean
    public Docket createRestApi()
    {
        ParameterBuilder ticketPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        ticketPar.name("Authorization").description("默认为空即可")
                .modelRef(new ModelRef("string")).parameterType("header")
                .required(false).build(); //header中的ticket参数非必填，传空也可以
        pars.add(ticketPar.build());    //根据每个方法名也知道当前方法在设置什么参数
        SwaggerConfig instace = SwaggerConfig.getInstance();
        List<String> controllerPackagePath = instace.getControllerPackagePath();
        List<Predicate<RequestHandler>> pres = new ArrayList<>();
        for (String s : controllerPackagePath)
        {
            Predicate<RequestHandler> pre = RequestHandlerSelectors.basePackage(s);
            pres.add(pre);
        }
        Docket docket = null;
        ApiSelectorBuilder select = new Docket(DocumentationType.SWAGGER_2)
                .select();
        for (Predicate<RequestHandler> requestHandlerPredicate : pres)
        {
            select = select.apis(requestHandlerPredicate);
        }
        if (!instace.isEnableSwagger())
        {
            docket = select
                    .paths(PathSelectors.none())//如果是线上环境，添加路径过滤，设置为全部都不符合
                    .build()
                    .globalOperationParameters(pars)
                    .apiInfo(apiInfo());
        } else
        {
            docket = new Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .build()
                    .globalOperationParameters(pars)
                    .apiInfo(apiInfo());
        }
        return docket;
    }

    //构建 api文档的详细信息函数,注意这里的注解引用的是哪个
    private ApiInfo apiInfo()
    {
        SwaggerConfig instance = SwaggerConfig.getInstance();
        return new ApiInfoBuilder()
                //页面标题
                .title(instance.getTitle())
                //创建人
//                .contact("MarcopoloStatistics Developer")
                //版本号
                .version("1.0")
                //描述
                .description("DESC")
                .build();
    }
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry)
//    {
//        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
//        registry.addResourceHandler("swagger-ui.html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("doc.html")
//                .addResourceLocations("classpath:/META-INF/resources/");
//        registry.addResourceHandler("/webjars/**")
//                .addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }
}
