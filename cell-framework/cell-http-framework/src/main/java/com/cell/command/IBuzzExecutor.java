package com.cell.command;

import com.cell.bo.BuzzContextBO;
import com.cell.context.IHttpCommandContext;
import com.cell.serialize.ISerializable;

import java.io.IOException;

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
    void execute(BuzzContextBO bo) throws IOException;
}
