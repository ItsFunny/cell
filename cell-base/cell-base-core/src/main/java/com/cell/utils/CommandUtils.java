package com.cell.utils;

import com.cell.annotations.Command;
import com.cell.protocol.ICommand;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-30 10:28
 */
public class CommandUtils
{
    public static Command getCommandAnno(Class<? extends ICommand> commandClz)
    {
        Command anno = commandClz.getAnnotation(Command.class);
        if (anno == null)
        {
            throw new RuntimeException(commandClz + "未添加@Command注解，无法初始化命令参数");
        }
        return anno;
    }
//    public static String getSequenceId(ICommand cmd)
//    {
//        if (cmd == null)
//        {
//            return null;
//        }
//        String sequenceId = cmd.getId();
//        if (StringUtils.isEmpty(sequenceId) || "0".equals(sequenceId))
//        {
//            sequenceId = cmd.getCtx().getHttpSummary().getSequenceId();
//        }
//        return sequenceId;
//    }
}
