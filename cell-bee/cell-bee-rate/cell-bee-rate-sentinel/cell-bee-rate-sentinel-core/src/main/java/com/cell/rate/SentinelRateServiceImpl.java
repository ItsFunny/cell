package com.cell.rate;

import com.alibaba.csp.sentinel.AsyncEntry;
import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.ResourceTypeConstants;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.cell.base.core.annotations.ActivePlugin;
import com.cell.bee.rate.base.IRateAcquireBody;
import com.cell.bee.rate.base.IRateEntry;
import com.cell.bee.rate.base.IRateService;
import com.cell.bee.rate.base.exception.RateBlockException;
import lombok.Builder;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-12 07:35
 */
@ActivePlugin
public class SentinelRateServiceImpl implements IRateService
{
    @Data
    @Builder
    public static class SentinelRateBody implements IRateAcquireBody
    {
        private String resourceName;
        private Object[] params;
    }

    @Override
    public IRateEntry acquire(IRateAcquireBody iB) throws RateBlockException
    {
        SentinelRateBody body = (SentinelRateBody) iB;
        try
        {
            AsyncEntry entry = SphU.asyncEntry(body.getResourceName(), ResourceTypeConstants.COMMON_API_GATEWAY,
                    EntryType.IN, body.getParams());
            return new SentinelRateEntry.SentinelRateEntryBuilder().entry(entry).params(body.getParams()).build();
        } catch (BlockException e)
        {
            throw new RateBlockException(e);
        }
    }

    @Override
    public void release(IRateEntry entry)
    {

    }
}
