package com.cell.component.httpsecurity.security.impl;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2022/4/11 2:01 下午
 */
public interface BaseTemplate<T>
{
    boolean validIsMine(T type);
}
