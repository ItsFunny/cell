package com.cell.context;

import com.cell.base.core.protocol.IContext;
import lombok.Data;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-21 14:31
 */
@Data
public class MetricsContext implements IContext
{
    private long startTime;
    private ServerWebExchange exchange;

    @Override
    public void discard() { }

    public MetricsContext()
    {
        this.startTime = System.currentTimeMillis();
    }

}
