package com.cell.services;

/**
 * @author Charlie
 * @When
 * @Description  FROM 转换为Model
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-12-10 12:02
 */
public interface IFrom<F,M>
{
    M from(F f);
}
