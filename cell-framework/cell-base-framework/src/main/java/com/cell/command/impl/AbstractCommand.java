package com.cell.command.impl;

import com.cell.command.ICommand;
import lombok.Data;

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
    // 当前调用方
    protected ICommand current;
    // 对等方
    protected ICommand couple;


}
