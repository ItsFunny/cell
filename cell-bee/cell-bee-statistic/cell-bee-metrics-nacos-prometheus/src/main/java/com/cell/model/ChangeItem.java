package com.cell.model;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-22 21:21
 */
@Data
@AllArgsConstructor
public class ChangeItem<T>
{
    private T item;
    private long changeIndex;
}
