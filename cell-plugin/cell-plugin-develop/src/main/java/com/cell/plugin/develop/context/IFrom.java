package com.cell.plugin.develop.context;

/**
 * @author Charlie
 * @When
 * @Description FROM 转换为Model
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-12-10 12:02
 */
@Deprecated
public interface IFrom<F>
{
    void from(F f);
}
