package com.cell.event;

import com.cell.base.common.events.IEvent;
import lombok.Builder;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-15 05:39
 */
@Data
@Builder
public class StasticEvent implements IEvent
{
    private String method;
    private String uri;
    private long startTIme;
    private long endTime;
}
