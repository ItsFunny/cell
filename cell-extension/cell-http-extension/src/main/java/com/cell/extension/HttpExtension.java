package com.cell.extension;

import com.cell.annotations.Plugin;
import com.cell.context.INodeContext;
import com.cell.postprocessor.ReactorCache;
import com.cell.reactor.IDynamicHttpReactor;
import com.cell.service.IDynamicControllerService;
import com.cell.service.impl.DynamicControllerServiceImpl;

import java.util.Collection;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 06:48
 */
public class HttpExtension extends AbstractSpringNodeExtension
{
    private IDynamicControllerService dynamicControllerService;

    @Plugin
    public IDynamicControllerService dynamicControllerService()
    {
        return this.dynamicControllerService;
    }

    @Override
    public void init(INodeContext ctx) throws Exception
    {
        this.dynamicControllerService=new DynamicControllerServiceImpl();
    }

    @Override
    public void start(INodeContext ctx) throws Exception
    {
        Collection<IDynamicHttpReactor> reactors = ReactorCache.getReactors();
        for (IDynamicHttpReactor reactor : reactors)
        {
            this.dynamicControllerService.reigsterReactor(reactor);
        }
    }

    @Override
    public void ready(INodeContext ctx) throws Exception
    {

    }

    @Override
    public void close(INodeContext ctx) throws Exception
    {

    }
}
