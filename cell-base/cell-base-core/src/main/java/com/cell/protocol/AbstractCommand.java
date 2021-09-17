package com.cell.protocol;

import com.cell.annotations.Command;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.reactor.IReactor;
import com.cell.serialize.ISerializable;
import com.cell.utils.CommandUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 21:23
 */
@Data
public abstract class AbstractCommand implements ICommand
{
    // FIXME ,这个或许可以合并
    protected Command commandAnnotation;
    // 当前调用方
    protected ICommand current;
    // 对等方
    protected ICommand couple;

    private IHead head;

    private IBuzzContext ctx;


    private String id;

    private List<String> callStack = new ArrayList<>(1);

    public AbstractCommand()
    {
        this.commandAnnotation = CommandUtils.getCommandAnno(this.getClass());
        this.current = this;
        this.head = newHead();
    }

    protected abstract IHead newHead();

    // copy
    protected abstract void onMakeCouple(ICommand couple);


    public ContextResponseWrapper.ContextResponseWrapperBuilder baseComdResponseWrapper()
    {
        return ContextResponseWrapper.builder()
                .cmd(this);
    }

    @Override
    public ICommand couple()
    {
        Class<? extends ICommand> couple = this.commandAnnotation.couple();
        if (couple == ICommand.class)
        {
            return null;
        }
        try
        {
            ICommand ret = couple.newInstance();
            this.onMakeCouple(ret);
            return ret;
        } catch (Exception e)
        {
            LOG.erroring(Module.CONTAINER, "makeCouple 失败", e.getMessage());
            return null;
        }
    }

    //    @Override
//    public ICommand couple()
//    {
//        Class<? extends ICommand> couple = commandAnnotation.couple();
//        if (couple == null)
//        {
//            return null;
//        }
//        try
//        {
//            ICommand res = couple.newInstance();
//            return res;
//        } catch (Exception e)
//        {
//            // FIXME
//            throw new RuntimeException("easd", e);
//        }
//    }
    @Override
    public ISerializable getBO(IBuzzContext context)
    {
        return null;
    }
}
