package com.cell.wrapper;

import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-16 22:14
 */
@Data
public class MonoWrapper<T>
{
    T ret;
}
