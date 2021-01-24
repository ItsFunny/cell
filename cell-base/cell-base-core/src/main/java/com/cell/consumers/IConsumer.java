package com.cell.consumers;

import com.cell.enums.ConsumeEnums;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-13 22:57
 */
public interface IConsumer<T,V>
{
    V consume(T t);
}
