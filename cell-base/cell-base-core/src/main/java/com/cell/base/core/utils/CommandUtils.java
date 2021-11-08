package com.cell.base.core.utils;

import com.cell.base.core.annotations.Command;
import com.cell.base.core.annotations.ReactorAnno;
import com.cell.base.core.protocol.ICommand;
import com.cell.base.core.reactor.ICommandReactor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        Command anno = ClassUtil.getMergedAnnotation(commandClz, Command.class);
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

    public static Optional<List<Class<? extends ICommand>>> getReactorCommands(ICommandReactor reactor)
    {
        ReactorAnno annotation = ClassUtil.getMergedAnnotation(reactor.getClass(), ReactorAnno.class);
        Class<? extends ICommand>[] cmds = annotation.cmds();
        if (cmds.length == 0) return Optional.empty();
        return Optional.of(Stream.of(cmds).map(c -> (Class<? extends ICommand>) c).collect(Collectors.toList()));
    }
}
