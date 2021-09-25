package com.cell.manager;

import com.cell.annotations.ActiveMethod;
import com.cell.annotations.HttpCmdAnno;
import com.cell.annotations.ManagerNode;
import com.cell.annotations.ReactorAnno;
import com.cell.command.IHttpCommand;
import com.cell.hooks.IReactorExecutor;
import com.cell.manager.context.OnAddReactorContext;
import com.cell.manager.context.SelectByUriContext;
import com.cell.model.CommandWrapper;
import com.cell.models.Couple;
import com.cell.reactor.IHttpReactor;
import com.cell.utils.UriUtils;
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
    public IReactorExecutor onAddReactor()
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
            wrapper.setReactor(cc.getReactor());
            this.wrapperMap.put( uri, wrapper);
            cc.setSatisfy(true);
            return c.execute(ctx);
        };
    }

    @ActiveMethod(id = ReactorSelectorManager.selectByUri)
    public IReactorExecutor selectByUri()
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
                ret.setV1(wrapper.getCmd());
                ret.setV2(wrapper.getReactor());
                c.setRet(ret);
                return Mono.empty();
            }
            return ch.execute(ctx);
        };
    }
}
