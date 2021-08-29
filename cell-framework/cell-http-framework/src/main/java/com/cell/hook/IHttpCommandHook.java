package com.cell.hook;

import com.cell.chain.IChain;
import com.cell.command.IHttpCommand;
import com.cell.hooks.IDeltaChainHook;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail hook会通过@CellOrderer 进行自动排序组合,并且需要自动提供一个hookManager自动管理,注册
 * @Attention:
 * @Date 创建时间：2021-08-28 11:01
 */
public interface IHttpCommandHook extends IDeltaChainHook<HookCommandWrapper, HttpCommandHookResult>
{
    void registerNext(IHttpCommandHook next);
}
