package com.cell.bo;

import com.cell.command.IHttpCommand;
import com.cell.context.IHttpContext;
import com.cell.serialize.ISerializable;
import lombok.Builder;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-02 21:19
 */
@Data
public class BuzzContextBO
{
    private IHttpContext context;
    private ISerializable bo;
    private IHttpCommand cmd;

    public BuzzContextBO(IHttpContext context, ISerializable bo, IHttpCommand cmd)
    {
        this.context = context;
        this.bo = bo;
        this.cmd = cmd;
    }
}
