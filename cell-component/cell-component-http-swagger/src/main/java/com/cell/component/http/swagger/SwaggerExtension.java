package com.cell.component.http.swagger;

import com.cell.base.common.constants.OrderConstants;
import com.cell.base.core.annotations.CellOrder;
import com.cell.component.http.swagger.config.SwaggerConfig;
import com.cell.node.core.context.INodeContext;
import com.cell.node.spring.exntension.AbstractSpringNodeExtension;
import org.springframework.context.annotation.Bean;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

import java.util.ArrayList;
import java.util.List;

@CellOrder(value = OrderConstants.MAX_ORDER)
public class SwaggerExtension extends AbstractSpringNodeExtension
{
//    private Docket docket;

//    @Bean
//    public Docket createRestApi()
//    {
//        return this.docket;
//    }

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

    @Override
    protected void onInit(INodeContext ctx) throws Exception
    {
        SwaggerConfig.getInstance().seal(ctx);

//        ParameterBuilder ticketPar = new ParameterBuilder();
//        List<Parameter> pars = new ArrayList<Parameter>();
//        ticketPar.name("Authorization").description("默认为空即可")
//                .modelRef(new ModelRef("string")).parameterType("header")
//                .required(false).build(); //header中的ticket参数非必填，传空也可以
//        pars.add(ticketPar.build());    //根据每个方法名也知道当前方法在设置什么参数
//        SwaggerConfig instace = SwaggerConfig.getInstance();
//        if (!instace.isEnableSwagger())
//        {
//            this.docket = new Docket(DocumentationType.SWAGGER_2)
//                    .select()
//                    .apis(RequestHandlerSelectors.basePackage(instace.getControllerPackagePath()))
//                    .paths(PathSelectors.none())//如果是线上环境，添加路径过滤，设置为全部都不符合
//                    .build()
//                    .globalOperationParameters(pars)
//                    .apiInfo(apiInfo());
//        } else
//        {
//            this.docket = new Docket(DocumentationType.SWAGGER_2)
//                    .select()
//                    .apis(RequestHandlerSelectors.basePackage(instace.getControllerPackagePath()))
//                    .build()
//                    .globalOperationParameters(pars)
//                    .apiInfo(apiInfo());
//        }
    }

    @Override
    protected void onStart(INodeContext ctx) throws Exception
    {

    }

    @Override
    protected void onReady(INodeContext ctx) throws Exception
    {

    }

    @Override
    protected void onClose(INodeContext ctx) throws Exception
    {

    }
}
