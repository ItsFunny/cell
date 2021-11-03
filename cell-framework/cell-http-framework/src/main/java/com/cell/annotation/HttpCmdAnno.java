package com.cell.annotation;

import com.cell.annotations.Command;
import com.cell.enums.EnumHttpRequestType;
import com.cell.enums.EnumHttpResponseType;
import com.cell.models.Module;
import com.cell.reactor.IHttpReactor;
import com.cell.reactor.impl.DefaultHttpReactor;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author joker
 * @When
 * @Description
 * @Detail
 * @date 创建时间：2021-08-29 06:27
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Command(reactor = DefaultHttpReactor.class, protocol = "/protocol")
public @interface HttpCmdAnno
{
    EnumHttpRequestType requestType() default EnumHttpRequestType.HTTP_POST;

    EnumHttpResponseType responseType() default EnumHttpResponseType.HTTP_JSON;

    @AliasFor(annotation = Command.class, attribute = "protocol")
    String uri();

    String module() default "UNKNOWN";


    @AliasFor(annotation = Command.class, attribute = "buzzClz")
    Class<?> buzzClz() default Void.class;


    @AliasFor(annotation = Command.class, attribute = "reactor")
    Class<? extends IHttpReactor> reactor() default DefaultHttpReactor.class;

    String viewName() default "";

    boolean websocket() default false;
}
