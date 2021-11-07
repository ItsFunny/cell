package com.cell.http.gate.extension;

import com.cell.annotations.ActivePlugin;
import com.cell.annotations.Required;
import com.cell.context.INodeContext;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-26 05:40
 */
public abstract class AbstractNodeExtension implements INodeExtension
{
    protected ActivePlugin anno;
    protected boolean required = true;
    protected Required requiredAnno;
    protected byte status;

    public AbstractNodeExtension()
    {
        anno = this.getClass().getAnnotation(ActivePlugin.class);
        requiredAnno = this.getClass().getAnnotation(Required.class);
        if (requiredAnno != null)
        {
            required = requiredAnno.necessary();
        }
        this.status = INodeExtension.zero;
    }

    @Override
    public void setUnrequired()
    {
        required = false;
    }

    @Override
    public boolean isRequired()
    {
        return this.required;
    }

    @Override
    public String getName()
    {
        if (anno != null && !anno.name().isEmpty())
        {
            return anno.name();
        }
        return this.getClass().getSimpleName();
    }


    @Override
    public Object loadConfiguration(INodeContext ctx) throws Exception
    {
        if (this.status >= INodeExtension.init)
        {
            return null;
        }
        return this.onLoadExtensionConfiguration(ctx);
    }

    // TODO, 修改为abs
    protected Object onLoadExtensionConfiguration(INodeContext ctx) throws Exception
    {
        return null;
    }


    protected abstract void onInit(INodeContext ctx) throws Exception;

    protected abstract void onStart(INodeContext ctx) throws Exception;

    protected abstract void onReady(INodeContext ctx) throws Exception;

    protected abstract void onClose(INodeContext ctx) throws Exception;

    private void nextStep()
    {
        this.status <<= 1;
    }

    @Override
    public void init(INodeContext ctx) throws Exception
    {
        if (this.status >= INodeExtension.init)
        {
            return;
        }
        this.onInit(ctx);
        this.nextStep();
    }

    @Override
    public void start(INodeContext ctx) throws Exception
    {
        if (this.status >= INodeExtension.start)
        {
            return;
        }
        this.onStart(ctx);
        this.nextStep();
    }

    @Override
    public void ready(INodeContext ctx) throws Exception
    {
        if (this.status >= INodeExtension.ready)
        {
            return;
        }
        this.onReady(ctx);
        this.nextStep();
    }

    @Override
    public void close(INodeContext ctx) throws Exception
    {
        if (this.status >= INodeExtension.close)
        {
            return;
        }
        this.onClose(ctx);
        this.nextStep();
    }
}
