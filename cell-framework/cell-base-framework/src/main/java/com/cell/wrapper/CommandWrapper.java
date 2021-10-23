package com.cell.wrapper;

import com.cell.annotations.Command;
import com.cell.protocol.ICommand;
import com.cell.reactor.ICommandReactor;
import com.cell.utils.ClassUtil;
import lombok.Data;

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



    public void setCmd(Class<? extends ICommand> cmd)
    {
        this.cmd = cmd;
        this.commandAnno= ClassUtil.getMergedAnnotation(cmd,Command.class);
    }
}
