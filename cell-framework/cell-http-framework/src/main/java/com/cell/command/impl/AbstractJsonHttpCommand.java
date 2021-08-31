package com.cell.command.impl;

import com.cell.context.IHttpContext;
import com.cell.protocol.ICommandExecuteResult;
import com.cell.serialize.IInputArchive;
import com.cell.serialize.IOutputArchive;
import com.cell.serialize.ISerializable;
import com.cell.serialize.JsonInput;
import com.cell.util.HttpUtils;
import io.netty.handler.codec.http.HttpUtil;
import lombok.Data;

import javax.annotation.sql.DataSourceDefinitions;
import java.io.IOException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-30 11:11
 */
@Data
public abstract class AbstractJsonHttpCommand extends AbstractHttpCommand
{
    private String jsonString;
    public AbstractJsonHttpCommand()
    {
        super();
    }
    @Override
    protected ICommandExecuteResult onExecute(IHttpContext ctx) throws IOException
    {
        this.jsonString = HttpUtils.readStringFromRequest(ctx.getHttpRequest());
        ISerializable bo = this.getBO();
        if (bo != null)
        {
            bo.read(JsonInput.createArchive(this.jsonString));
        }
        return this.doExecuteDirectly(ctx, bo);
    }

    protected abstract ICommandExecuteResult doExecuteDirectly(IHttpContext ctx, ISerializable bo) throws IOException;
}
