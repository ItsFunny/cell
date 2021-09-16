package com.cell.hook;

import com.cell.annotations.AutoPlugin;
import com.cell.annotations.ManagerNode;
import com.cell.constant.HookConstants;
import com.cell.constants.BitConstants;
import com.cell.reactor.IHttpReactor;
import com.cell.utils.ReflectionUtils;
import com.cell.wrapper.MonoWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-28 22:12
 */
@ManagerNode(group = HookConstants.GROUP_CMD_HOOK, name = HookConstants.NAME_HOOK, orderValue = Integer.MAX_VALUE)
public class CommandExecuteHook extends AbstractHttpCommandHook
{
    @Override
    protected HttpCommandHookResult onDeltaHook(HookCommandWrapper wrapper)
    {
        IHttpReactor reactor = wrapper.getReactor();
        reactor.execute(wrapper.getContext());
        HttpCommandHookResult res = new HttpCommandHookResult();
        res.setContext(wrapper.getContext());
        return res;
    }

    @Override
    protected void onTrackEnd(HttpCommandHookResult res)
    {

    }

    @Override
    protected void onExceptionCaught(Exception e)
    {

    }

    public static void main(String[] args)
    {
        Mono<MonoWrapper<Boolean>> monoWrapperMono = ReflectionUtils.containAnnotaitonsInFieldOrMethod(CommandExecuteHook.class, BitConstants.or, AutoPlugin.class, Autowired.class);
        monoWrapperMono.subscribe((v) -> System.out.println(v));
    }

}
