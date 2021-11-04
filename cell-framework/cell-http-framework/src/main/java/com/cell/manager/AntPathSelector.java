package com.cell.manager;

import com.cell.grpc.client.base.framework.annotation.ActiveMethod;
import com.cell.grpc.client.base.framework.annotation.HttpCmdAnno;
import com.cell.annotations.ManagerNode;
import com.cell.grpc.client.base.framework.command.IHttpCommand;
import com.cell.executor.IBaseReactorExecutor;
import com.cell.manager.context.OnAddReactorContext;
import com.cell.manager.context.SelectByUriContext;
import com.cell.models.Couple;
import com.cell.protocol.CommandWrapper;
import com.cell.reactor.ICommandReactor;
import com.cell.reactor.IHttpReactor;
import org.springframework.util.AntPathMatcher;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-24 05:46
 */
@ManagerNode(group = ReactorSelectorManager.reacotrSelector, name = "placeHolder")
public class AntPathSelector
{
    private Map<String, CommandWrapper> wrapperMap = new HashMap<>(1);
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @ActiveMethod(id = ReactorSelectorManager.onAddReactor)
    public IBaseReactorExecutor onAddReactor()
    {
        return (ctx, c) ->
        {
            OnAddReactorContext cc = (OnAddReactorContext) ctx;
            HttpCmdAnno anno = cc.getAnno();
            String uri = anno.uri();
            boolean pattern = antPathMatcher.isPattern(uri);
            if (!pattern) return c.execute(ctx);
            cc.setSatisfy(true);
            CommandWrapper wrapper = new CommandWrapper();
            wrapper.setCmd(cc.getCmd());
            wrapper.setReactor((ICommandReactor) cc.getReactor());
            this.wrapperMap.put( uri, wrapper);
            cc.setSatisfy(true);
            return c.execute(ctx);
        };
    }

    @ActiveMethod(id = ReactorSelectorManager.selectByUri)
    public IBaseReactorExecutor selectByUri()
    {
        return (ctx, ch) ->
        {
            SelectByUriContext c = (SelectByUriContext) ctx;
            String uri = c.getUri();
            for (String uriP : wrapperMap.keySet())
            {
                if (!antPathMatcher.match(uriP, uri))
                {
                    continue;
                }
                Couple<Class<? extends IHttpCommand>, IHttpReactor> ret = new Couple<>();
                CommandWrapper wrapper = this.wrapperMap.get(uriP);
                ret.setV1((Class<? extends IHttpCommand>) wrapper.getCmd());
                ret.setV2((IHttpReactor) wrapper.getReactor());
                c.setRet(ret);
                return Mono.empty();
            }
            return ch.execute(ctx);
        };
    }
}
