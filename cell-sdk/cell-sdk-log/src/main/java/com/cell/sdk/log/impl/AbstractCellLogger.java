package com.cell.sdk.log.impl;

import com.cell.base.common.context.AbstractInitOnce;
import com.cell.base.common.decorators.DefaultStatefulDecoratorManager;
import com.cell.base.common.enums.TypeEnums;
import com.cell.base.common.models.Module;
import com.cell.base.common.models.ModuleInterface;
import com.cell.base.common.services.TypeFul;
import com.cell.base.common.utils.CollectionUtils;
import com.cell.sdk.log.*;
import com.cell.sdk.log.constants.LogConstant;
import lombok.Data;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-01-18 23:23
 */
@Data
public abstract class AbstractCellLogger extends AbstractInitOnce implements ICellLogger, TypeFul<Serializable>
{
    // 1. 通过logLevel过滤
    private LogLevel logLevel;
    private Module module;
    private List<String> blackList;


    protected abstract Set<ILogConsumer> getLogConsumers(ModuleInterface module, LogTypeEnums logType, LogLevel logLevel);

    public abstract void registerConsumers(ILogConsumer consumer);

    protected Optional<ILogHook> afterCreateHook = Optional.of((entry) ->
            DefaultStatefulDecoratorManager.getInstance().decorate(entry, (e) -> (e.getStatus() & TypeEnums.LOG.getStatus()) > 0));

    public AbstractCellLogger()
    {
        this.blackList = Arrays.asList(
                "com.cell.sdk.log.impl.CellLoggerContext",
                "com.cell.sdk.log.impl.AbstractCellLogger",
                "Slf4JLogger",
                "org.apache.cxf.common.logging",
                "com.cell.sdk.log.LOG",
                "com.cell.sdk.log.bridge.LOGLoggerWrapper"
        );
    }

    public void addBlack(String black)
    {
        this.blackList.add(black);
    }

    // FIXME 配置


    @Override
    public void info(LogTypeEnums logType, Throwable err, String format, Object... data)
    {
        this.log(this.module, LogConstant.DEFAULT_SEQUENCE, LogLevel.INFO, logType, err, format, data);
    }

    @Override
    public void warn(LogTypeEnums logType, Throwable err, String format, Object... data)
    {
        this.log(this.module, LogConstant.DEFAULT_SEQUENCE, LogLevel.WARN, logType, err, format, data);
    }

    @Override
    public void error(LogTypeEnums logType, Throwable err, String format, Object... data)
    {
        this.log(this.module, LogConstant.DEFAULT_SEQUENCE, LogLevel.ERROR, logType, err, format, data);
    }

    public void log(ModuleInterface module, String sequenceId, LogLevel logLevel, LogTypeEnums logType, Throwable err, String format, Object... data)
    {
// 1. 通过自身的logLevel 过滤,可以认为是全局的logLevel
        if (!this.logAble(logLevel)) return;
        Set<ILogConsumer> logConsumers = this.getLogConsumers(module, logType, logLevel);
        if (CollectionUtils.isEmpty(logConsumers))
        {
            return;
        }
        final LogEntry logEntry = CellLoggerContext.createLogEntry(module, sequenceId, this.blackList, logLevel, logType, err, afterCreateHook, format, data);
        final DefaultLogEventWrapper defaultLogEventWrapper = new DefaultLogEventWrapper(logEntry);
        logConsumers.stream().filter(c -> c.logAble(logEntry)).forEach(c ->
                c.consume(defaultLogEventWrapper));
    }

    protected boolean logAble(LogLevel logLevel)
    {
        return !this.logLevel.isBigger(logLevel);
    }
}
