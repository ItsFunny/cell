package com.cell.model;

import com.cell.base.common.events.IEvent;
import lombok.Builder;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-14 22:02
 */
@Builder
public class ErrorResponseEvent implements IEvent
{
    private String test;

}
