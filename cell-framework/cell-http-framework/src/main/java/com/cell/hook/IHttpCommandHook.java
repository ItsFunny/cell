package com.cell.hook;

import com.cell.hooks.IDeltaChainTracker;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail hook会通过@CellOrderer 进行自动排序组合,并且需要自动提供一个hookManager自动管理,注册
 * @Attention:
 * @Date 创建时间：2021-08-28 11:01
 */
public interface IHttpCommandHook extends IDeltaChainTracker<HookCommandWrapper, HttpCommandHookResult>
{
    void registerNext(IHttpCommandHook next);

    void registerPrev(IHttpCommandHook prev);

    default IHttpCommandHook revert()
    {
        IDeltaChainTracker<HookCommandWrapper, HttpCommandHookResult> ret = this;
        IDeltaChainTracker<HookCommandWrapper, HttpCommandHookResult> tmp = this;
        while (null != tmp)
        {
            tmp = tmp.next();
            ((IHttpCommandHook) ret).registerNext((IHttpCommandHook) tmp);
            ret = tmp;
        }
        return (IHttpCommandHook) ret;
    }
}
