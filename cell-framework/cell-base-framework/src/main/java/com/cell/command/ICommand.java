package com.cell.command;

import com.cell.model.CommandExecuteResult;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-28 11:03
 */
public interface ICommand
{
    ICommandExecuteResult execute();

    ICommand getCouple();


    default String getId()
    {
        return "0";
    }

    void setId(String id);
}
