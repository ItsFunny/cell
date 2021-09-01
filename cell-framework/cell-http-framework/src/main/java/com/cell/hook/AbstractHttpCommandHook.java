package com.cell.hook;


import com.cell.config.AbstractInitOnce;
import com.cell.context.InitCTX;
import com.cell.hooks.IDeltaChainTracker;
import com.cell.utils.CollectionUtils;
import org.apache.lucene.index.TieredMergePolicy;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-28 11:06
 */
public abstract class AbstractHttpCommandHook extends AbstractInitOnce implements IHttpCommandHook
{
    protected abstract HttpCommandHookResult onDeltaHook(HookCommandWrapper wrapper);

    protected abstract void onTrackEnd(HttpCommandHookResult res);

    protected abstract void onExceptionCaught(Exception e);

    private IHttpCommandHook next;
    private IHttpCommandHook last;

    private boolean active = true;

    @Override
    public HttpCommandHookResult trackBegin(HookCommandWrapper wrapper)
    {
        try
        {
            HttpCommandHookResult res = this.onDeltaHook(wrapper);
            if (null != this.next && this.next.active())
            {
                wrapper.setLastResult(res);
                res = this.next.trackBegin(wrapper);
            }
            return res;
        } catch (Exception e)
        {
            this.onExceptionCaught(e);
            throw e;
        }
    }


    @Override
    public void trackEnd(HttpCommandHookResult res)
    {
        try
        {
            this.onTrackEnd(res);
            if (null != this.last && this.last.active())
            {
                this.last.trackEnd(res);
            }
        } catch (Exception e)
        {
            this.onExceptionCaught(e);
            throw e;
        }
    }

    @Override
    public void exceptionCaught(Exception e)
    {
        this.onExceptionCaught(e);
        if (null != this.next && this.next.active())
        {
            this.next.exceptionCaught(e);
        }
    }

    @Override
    public void registerNext(IHttpCommandHook next)
    {
        if (this == next)
        {
            return;
        }
        if (null == this.next)
        {
            this.next = next;
        } else
        {
            IHttpCommandHook temp = this.next;
            while (null != temp.next())
            {
                temp = (IHttpCommandHook) temp.next();
            }
            temp.registerNext(next);
        }
    }

    @Override
    public boolean active()
    {
        return this.active;
    }


    @Override
    public IDeltaChainTracker<HookCommandWrapper, HttpCommandHookResult> prev()
    {
        return this.last;
    }

    @Override
    public IDeltaChainTracker<HookCommandWrapper, HttpCommandHookResult> next()
    {
        return this.next;
    }

    @Override
    public void registerPrev(IHttpCommandHook last)
    {
        if (last == this)
        {
            return;
        }
        if (null == this.last)
        {
            this.last = last;
        } else
        {
            IHttpCommandHook temp = this.last;
            while (null != temp.prev())
            {
                temp = (IHttpCommandHook) temp.prev();
            }
            temp.registerPrev(last);
        }
    }

    @Override
    protected void onInit(InitCTX ctx)
    {
        List<IHttpCommandHook> lasts = new ArrayList<>();

        IHttpCommandHook tmp = this;
        while (null != tmp)
        {
            lasts.add(tmp);
            tmp = (IHttpCommandHook) tmp.next();
        }
        if (CollectionUtils.isEmpty(lasts)) return;
        for (int i = lasts.size() - 1; i >= 0; i--)
        {
            this.registerPrev(lasts.get(i));
        }
    }
}
