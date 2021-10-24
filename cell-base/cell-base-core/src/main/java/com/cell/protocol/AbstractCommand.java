package com.cell.protocol;

import com.cell.annotations.Command;
import com.cell.annotations.Optional;
import com.cell.concurrent.base.EventExecutor;
import com.cell.constants.ContextConstants;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.serialize.IInputArchive;
import com.cell.serialize.IOutputArchive;
import com.cell.serialize.ISerializable;
import com.cell.utils.CommandUtils;
import lombok.Data;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.io.IOException;
import java.lang.reflect.Field;
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

    @Override
    public void execute(IBuzzContext ctx)
    {
        this.ctx = ctx;
        boolean async = this.commandAnnotation.async();
        if (async)
        {
            EventExecutor eventExecutor = ctx.getEventExecutor();
            eventExecutor.execute(this::fire);
        } else
        {
            this.fire();
        }
    }

    private void fire()
    {
        try
        {
            Class<?> bzClz = this.commandAnnotation.buzzClz();
            Object bo = null;
            if (bzClz != Void.class)
            {
                bo = this.newInstance(ctx, bzClz);
            }
            this.doExecute(ctx, bo);
        } catch (Exception e)
        {
            ctx.response(this.createResponseWp().exception(e).status(ContextConstants.FAIL).build());
        }
    }

    protected Object newInstance(IBuzzContext ctx, Class<?> bzClz) throws Exception
    {
        Object instance = null;
        IInputArchive inputArchive = this.getInputArchiveFromCtx(ctx);
        if (ISerializable.class.isAssignableFrom(bzClz))
        {
            instance = bzClz.newInstance();
            ((ISerializable) instance).read(inputArchive);
        } else
        {
            instance = this.reflectFill(bzClz, inputArchive);
        }
        return instance;
    }

    protected abstract IInputArchive getInputArchiveFromCtx(IBuzzContext c) throws Exception;

    protected abstract void doExecute(IBuzzContext ctx, Object bo) throws IOException;

    protected Object reflectFill(Class<?> clz, IInputArchive inputArchive) throws IOException
    {
        Field[] fields = clz.getDeclaredFields();
        BeanWrapper beanWrapper = new BeanWrapperImpl(clz);
        for (Field field : fields)
        {
            String name = field.getName();
            if (name.startsWith("abs")) continue;
            Optional annotation = field.getAnnotation(Optional.class);
            if (annotation == null)
            {
                beanWrapper.setPropertyValue(name, inputArchive.readString(name));
            } else
            {
                beanWrapper.setPropertyValue(name, inputArchive.readStringNullable(name));
            }
        }
        return beanWrapper.getWrappedInstance();
    }

    protected ContextResponseWrapper.ContextResponseWrapperBuilder createResponseWp()
    {
        return this.baseComdResponseWrapper();
    }

    // 解析参数
    @Override
    public void read(IInputArchive input) throws IOException
    {

    }

    @Override
    public void write(IOutputArchive output) throws IOException
    {

    }
}
