package com.cell.commands;

import com.cell.protocol.ICommand;
import com.cell.protocol.ICommandExecuteResult;
import com.cell.command.impl.AbstractHttpCommand;
import com.cell.enums.EnumHttpRequestType;
import com.cell.enums.EnumHttpResponseType;
import com.cell.serialize.IInputArchive;
import com.cell.serialize.IOutputArchive;

import java.io.IOException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-30 11:07
 */
public class MyCommand extends AbstractHttpCommand
{


    @Override
    public ICommandExecuteResult execute()
    {
        return null;
    }


    @Override
    public void read(IInputArchive input) throws IOException
    {

    }

    @Override
    public void write(IOutputArchive output) throws IOException
    {

    }
}
