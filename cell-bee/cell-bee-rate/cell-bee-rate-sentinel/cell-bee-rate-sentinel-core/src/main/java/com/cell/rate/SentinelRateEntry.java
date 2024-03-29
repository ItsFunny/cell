package com.cell.rate;

import com.alibaba.csp.sentinel.AsyncEntry;
import com.cell.bee.rate.base.IRateEntry;
import lombok.Builder;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-12 08:13
 */
@Data
@Builder
public class SentinelRateEntry implements IRateEntry
{
    private AsyncEntry entry;
    private Object[] params;

    @Override
    public void release()
    {
        this.entry.exit(1, params);
    }
}
