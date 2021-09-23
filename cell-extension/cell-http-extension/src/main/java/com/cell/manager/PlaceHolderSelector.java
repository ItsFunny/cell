package com.cell.manager;

import com.cell.annotations.ActiveMethod;
import com.cell.annotations.HttpCmdAnno;
import com.cell.annotations.ManagerNode;
import com.cell.hooks.IReactorExecutor;
import com.cell.manager.context.OnAddReactorContext;
import com.cell.model.CommandWrapper;

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
public class PlaceHolderSelector
{
    private Map<String, CommandWrapper> wrapperMap;

    @ActiveMethod(id = ReactorSelectorManager.onAddReactor)
    public IReactorExecutor onAddReactor()
    {
        return (ctx, c) ->
        {
            OnAddReactorContext cc = (OnAddReactorContext) ctx;
            HttpCmdAnno anno = cc.getAnno();
            String uri = anno.uri();
            if (uri.contains("{"))
            {
                CommandWrapper wrapper = new CommandWrapper();
                wrapper.setCmd(cc.getCmd());
                wrapper.setReactor(cc.getReactor());
                if (this.wrapperMap == null)
                {
                    this.wrapperMap = new HashMap<>();
                }
                this.wrapperMap.put(uri, wrapper);
            }
            return c.execute(ctx);
        };
    }

    @ActiveMethod(id = ReactorSelectorManager.selectByUri)
    public IReactorExecutor selectByUri()
    {
        return (ctx, ch) ->
        {
            return ch.execute(ctx);
        };
    }
}
