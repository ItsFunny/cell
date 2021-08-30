package com.cell.hook;


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

    protected abstract void onExceptionCaught(Exception e);

    private IHttpCommandHook next;

    private boolean active = true;

    @Override
    public HttpCommandHookResult hook(HookCommandWrapper wrapper)
    {
        try
        {
            HttpCommandHookResult res = this.onDeltaHook(wrapper);
            if (null != this.next && this.next.active())
            {
                wrapper.setLastResult(res);
                res = this.next.hook(wrapper);
            }
            return res;
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
    }

    @Override
    public boolean active()
    {
        return this.active;
    }
}
