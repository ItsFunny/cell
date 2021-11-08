package com.cell.model;

import com.cell.base.core.events.IEvent;
import lombok.Builder;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-17 21:54
 */
@Data
@Builder
public class ExceptionEvent implements IEvent
{
    private Throwable throwable;

}
