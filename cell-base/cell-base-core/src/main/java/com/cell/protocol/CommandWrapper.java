package com.cell.protocol;

import com.cell.annotations.Command;
import com.cell.exceptions.ProgramaException;
import com.cell.reactor.ICommandReactor;
import com.cell.utils.ClassUtil;
import com.cell.utils.StringUtils;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 22:18
 */
@Data
public class CommandWrapper
{
    private ICommandReactor reactor;
    private Command commandAnno;
    private Class<? extends ICommand> cmd;
    private Method fallBackMethod;
    private CommandProtocolID protocolID;


    public static Object defaultEmptyFallBackMethod(IServerRequest request, IServerResponse response, Throwable throwable)
    {
        return "fallback";
    }


    public void setCmd(Class<? extends ICommand> cmd)
    {
        this.cmd = cmd;
        this.commandAnno = ClassUtil.getMergedAnnotation(cmd, Command.class);
        this.protocolID = new DefaultStringCommandProtocolID(this.commandAnno.protocol());
        String fallBackMethod = this.commandAnno.fallBackMethod();
        try
        {
            if (StringUtils.isEmpty(fallBackMethod))
            {
                this.fallBackMethod = CommandWrapper.class.getMethod("defaultEmptyFallBackMethod", IServerRequest.class, IServerResponse.class, Throwable.class);
            } else
            {
                this.fallBackMethod = cmd.getMethod(fallBackMethod, IServerRequest.class, IServerResponse.class, Throwable.class);
            }
        } catch (NoSuchMethodException e)
        {
            throw new ProgramaException("fallbackMethod 必须为特殊method");
        }
    }
}