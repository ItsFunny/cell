package com.cell.http.framework.command;

import com.cell.base.core.serialize.ISerializable;
import com.cell.http.framework.bo.BuzzContextBO;
import com.cell.http.framework.context.IHttpCommandContext;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-02 13:58
 */
public interface IBuzzExecutor
{
    default ISerializable serialize(IHttpCommandContext context)
    {
        return null;
    }

    // FIXME 添加参数
    void execute(BuzzContextBO bo);
}
