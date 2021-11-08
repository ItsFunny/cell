package com.cell.http.framework.manager.context;

import com.cell.base.common.models.Couple;
import com.cell.base.core.protocol.IContext;
import com.cell.http.framework.reactor.IHttpReactor;
import com.cell.http.framework.command.IHttpCommand;
import lombok.Builder;
import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-24 05:19
 */
@Data
@Builder
public class SelectByUriContext extends IContext.EmptyContext
{
    private String uri;
    private Couple<Class<? extends IHttpCommand>, IHttpReactor> ret;

    @Override
    public boolean done()
    {
        return ret != null;
    }
}
