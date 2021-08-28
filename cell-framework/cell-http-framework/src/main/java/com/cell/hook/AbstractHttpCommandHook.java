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

    private IHttpCommandHook next;

    private boolean active = true;

    @Override
    public HttpCommandHookResult hook(HookCommandWrapper wrapper)
    {
        HttpCommandHookResult res = this.onDeltaHook(wrapper);
        if (null != this.next && this.next.active())
        {
            wrapper.setLastResult(res);
            res = this.next.hook(wrapper);
        }
        return res;
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
