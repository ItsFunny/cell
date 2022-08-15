package com.cell.component.http.filter.manager;

import com.cell.base.core.annotations.Manager;
import com.cell.bee.event.center.AbstractEventCenter;
import com.cell.node.core.context.CellContext;
import com.cell.plugin.pipeline.manager.IReflectManager;

@Manager(name = FilterManager.CELL_FILTER)
public class FilterManager extends AbstractEventCenter
{
    public static final String CELL_FILTER = "CELL_FILTER";

    private static final FilterManager instance = new FilterManager();

    @Override
    protected void afterInvoke()
    {

    }

    @Override
    public IReflectManager createOrDefault()
    {
        return instance;
    }

    public static FilterManager getInstance()
    {
        return instance;
    }

    public static void dispatch(CellContext context)
    {
        try
        {
            instance.execute(context).block();
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }
}
