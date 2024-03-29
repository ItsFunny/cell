package com.cell.http.framework.manager;

import com.cell.base.common.models.Couple;
import com.cell.base.core.annotations.ManagerNode;
import com.cell.base.core.executor.IBaseReactorExecutor;
import com.cell.base.core.protocol.CommandWrapper;
import com.cell.base.core.reactor.ICommandReactor;
import com.cell.http.framework.annotation.HttpCmdAnno;
import com.cell.http.framework.manager.context.OnAddReactorContext;
import com.cell.http.framework.manager.context.SelectByUriContext;
import com.cell.http.framework.reactor.IHttpReactor;
import com.cell.plugin.pipeline.annotation.ActiveMethod;
import com.cell.http.framework.command.IHttpCommand;
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
            this.wrapperMap.put(uri, wrapper);
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
