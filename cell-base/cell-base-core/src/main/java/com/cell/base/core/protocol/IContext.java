package com.cell.base.core.protocol;

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
    default  void discard(){};

    default boolean done() {return false;}

    EmptyContext EMPTY_CONTEXT = new EmptyContext();

    class EmptyContext implements IContext
    {
        @Override
        public void discard()
        {
        }
    }
}
