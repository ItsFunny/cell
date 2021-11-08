package com.cell.node.core.extension;

import com.cell.node.core.context.INodeContext;
import org.apache.commons.cli.Options;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-10 21:17
 */
public interface INodeExtension
{
    byte zero = 1 << 0;
    byte init = 1 << 1;
    byte start = 1 << 2;
    byte ready = 1 << 3;
    byte close = 1 << 4;

    default String getName()
    {
        return this.getClass().getSimpleName();
    }

    void setUnrequired();

    default boolean isRequired() { return true; }

    Object loadConfiguration(INodeContext ctx) throws Exception;


    void init(INodeContext ctx) throws Exception;

    void start(INodeContext ctx) throws Exception;

    void ready(INodeContext ctx) throws Exception;

    void close(INodeContext ctx) throws Exception;

    default Options getOptions()
    {
        return null;
    }
}

