package com.cell.asm;

import net.bytebuddy.jar.asm.ClassVisitor;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-07-28 09:39
 */
public class CellLogAsm extends ClassVisitor
{

    public CellLogAsm(int i)
    {
        super(i);
    }

    public CellLogAsm(int i, ClassVisitor classVisitor)
    {
        super(i, classVisitor);
    }
}
