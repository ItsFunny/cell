package com.cell.annotation;

/**
 * @author joker
 * @When
 * @Description 被这个注解所包裹的类, 会被注册到discovery中
 * @Detail
 * @date 创建时间：2021-10-09 05:41
 */
public @interface RPCServer
{
    String group() default "";
}
