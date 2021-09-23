package com.cell.protocol;

import javax.swing.*;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-18 22:12
 */
public interface IContext
{
    void discard();
    default boolean done(){return false;}
    EmptyContext EMPTY_CONTEXT = new EmptyContext();


    class EmptyContext implements IContext
    {
        @Override
        public void discard()
        {

        }
    }
}
