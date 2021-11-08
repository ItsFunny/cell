package com.cell.reactor.abs;

import com.cell.base.common.constants.ProtocolConstants;
import com.cell.base.common.exceptions.ProgramaException;
import com.cell.cmd.IRPCServerCommand;
import com.cell.context.InitCTX;
import com.cell.protocol.ICommand;
import com.cell.reactor.AbstractBaseCommandReactor;
import com.cell.reactor.IRPCServerReactor;
import com.cell.rpc.grpc.client.framework.annotation.RPCServerReactorAnno;
import com.cell.rpc.server.base.annotation.RPCServerCmdAnno;
import com.cell.utils.ClassUtil;
import com.cell.utils.ReflectUtil;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-21 21:38
 */
public abstract class AbstractRPCServerReactor extends AbstractBaseCommandReactor implements IRPCServerReactor
{
    @Override
    protected void onInit(InitCTX ctx)
    {
        this.fillCmd(ctx);
        Class<? extends ICommand>[] cmds = this.getClass().getAnnotation(RPCServerReactorAnno.class).cmds();
        List<? extends Class<? extends IRPCServerCommand>> commandList = Stream.of(cmds).map(c ->
                (Class<? extends IRPCServerCommand>) c).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(commandList))
        {
            return;
        }
        commandList.stream().forEach(p ->
                this.registerCmd((ICommand) ReflectUtil.newInstance(p)));
    }

    @Override
    public void registerCmd(ICommand cmd)
    {

    }

    // FIXME ,这个需要删除
    private void fillCmd(InitCTX ctx)
    {
        Set<Class<? extends IRPCServerCommand>> httpCommandList = (Set<Class<? extends IRPCServerCommand>>) ctx.getData().get(
                ProtocolConstants.INIT_CTX_CMDS);
        if (CollectionUtils.isEmpty(httpCommandList)) return;
        RPCServerReactorAnno anno = ClassUtil.getMergedAnnotation(this.getClass(), RPCServerReactorAnno.class);
        httpCommandList.stream().forEach(c ->
        {
            RPCServerCmdAnno annotation = c.getAnnotation(RPCServerCmdAnno.class);
            if (annotation == null)
            {
                throw new ProgramaException("asd");
            }
        });
        List<Class<? extends IRPCServerCommand>> cmds = new ArrayList<>(httpCommandList);
        ReflectUtil.modify(this.getClass(), RPCServerReactorAnno.class, "cmds", cmds.toArray(new Class<?>[cmds.size()]));
    }
}
