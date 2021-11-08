package com.cell.http.framework.hook;

import com.cell.http.framework.context.IHttpCommandContext;
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
