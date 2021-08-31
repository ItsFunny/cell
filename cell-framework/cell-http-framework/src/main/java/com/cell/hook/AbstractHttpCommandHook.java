package com.cell.hook;


import com.cell.hooks.IDeltaChainTracker;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-28 11:06
 */
public abstract class AbstractHttpCommandHook implements IHttpCommandHook
{
    protected abstract HttpCommandHookResult onDeltaHook(HookCommandWrapper wrapper);

    protected abstract void onTrackEnd(HttpCommandHookResult res);

    protected abstract void onExceptionCaught(Exception e);

    private IHttpCommandHook next;
    private IHttpCommandHook prev;

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
            if (null != this.prev && this.prev.active())
            {
                this.prev.trackEnd(res);
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
        this.next = next;
        next.registerPrev(this);
    }

    @Override
    public boolean active()
    {
        return this.active;
    }


    @Override
    public IDeltaChainTracker<HookCommandWrapper, HttpCommandHookResult> prev()
    {
        return this.prev;
    }

    @Override
    public IDeltaChainTracker<HookCommandWrapper, HttpCommandHookResult> next()
    {
        return this.next;
    }

    @Override
    public void registerPrev(IHttpCommandHook prev)
    {
        this.prev = prev;
    }
}
