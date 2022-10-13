package com.cell.base.common.events;


import com.cell.base.common.empty.IEmpty;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-13 22:58
 */
public interface IEvent extends IEmpty
{
    String DEFAULT_SEQUENCE_ID="charlie";
    default String getSequenceId(){
        return DEFAULT_SEQUENCE_ID;
    }
}
