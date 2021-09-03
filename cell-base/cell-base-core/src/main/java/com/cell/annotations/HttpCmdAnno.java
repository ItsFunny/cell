package com.cell.annotations;

import com.cell.enums.EnumHttpRequestType;
import com.cell.enums.EnumHttpResponseType;
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
@Command(commandId = 1)
public @interface HttpCmdAnno
{
    EnumHttpRequestType requestType() default EnumHttpRequestType.HTTP_POST;

    EnumHttpResponseType responseType() default EnumHttpResponseType.HTTP_JSON;

    String uri();

    @AliasFor(annotation = Command.class, attribute = "commandId")
    short httpCommandId();

    String viewName() default "";

    String group() default "group";

    boolean websocket() default false;
}
