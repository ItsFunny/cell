package com.cell.utils;

import com.alibaba.csp.sentinel.context.ContextUtil;
import com.cell.IRateEntry;
import com.cell.constants.SentinelConstants;
import org.springframework.web.server.ServerWebExchange;

import java.util.Deque;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-15 04:36
 */
public class RateUtils
{
    public static void exitEntry(ServerWebExchange exchange)
    {
        Deque<IRateEntry> queue = exchange.getAttribute(SentinelConstants.ENTINEL_ENTRIES_KEY);
        if (queue != null && !queue.isEmpty())
        {
            IRateEntry entry = null;
            while (!queue.isEmpty())
            {
                entry = queue.pop();
                exit(entry);
            }
            exchange.getAttributes().remove(SentinelConstants.ENTINEL_ENTRIES_KEY);
        }
        ContextUtil.exit();
    }

    static void exit(IRateEntry entry)
    {
        entry.release();
    }

}
