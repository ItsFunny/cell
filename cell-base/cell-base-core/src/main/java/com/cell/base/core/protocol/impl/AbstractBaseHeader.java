package com.cell.base.core.protocol.impl;

import com.cell.base.core.protocol.ICommand;
import com.cell.base.core.protocol.IHead;
import com.cell.base.core.serialize.IInputArchive;
import com.cell.base.core.serialize.IOutputArchive;
import com.cell.base.core.utils.CommandUtils;
import lombok.Data;

import java.io.IOException;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-30 12:52
 */
@Data
public abstract class AbstractBaseHeader implements IHead
{
    //    private ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
    private String protocol;

    private String sequenceId;

    public AbstractBaseHeader(ICommand command)
    {
//         用于跟踪command
        this.protocol = CommandUtils.getCommandAnno(command.getClass()).protocol();
    }

    @Override
    public void read(IInputArchive input) throws IOException
    {
//        final Integer byteOrderInt =  input.readInteger("byteOrder");
//        if (Integer.valueOf(1).equals(byteOrderInt)){
//            this.byteOrder = ByteOrder.LITTLE_ENDIAN;
//        }else{
//            this.byteOrder = ByteOrder.BIG_ENDIAN;
//        }
        this.protocol = input.readString("protocol");
        this.sequenceId = input.readString("sequenceId");
    }

    @Override
    public void write(IOutputArchive output) throws IOException
    {
        output.writeString("protocol", protocol);
        output.writeString("sequenceId", sequenceId);
    }
}
