package com.cell.rate;

import com.alibaba.csp.sentinel.EntryType;
import com.alibaba.csp.sentinel.ResourceTypeConstants;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.cell.IRateAcquireBody;
import com.cell.IRateEntry;
import com.cell.IRateService;
import com.alibaba.csp.sentinel.AsyncEntry;
import com.cell.exception.RateBlockException;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-12 07:35
 */
public class SentinelRateServiceImpl implements IRateService
{
    @Data
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
            return new SentinelRateEntry.SentinelRateEntryBuilder().entry(entry).build();
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
