package com.cell.hook;

import com.cell.context.IHttpCommandContext;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-28 11:16
 */
@Data
public class HttpCommandHookResult
{
    private IHttpCommandContext context;

}
