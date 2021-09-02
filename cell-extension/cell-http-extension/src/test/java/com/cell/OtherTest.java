package com.cell;

import cn.tass.util.Strings;
import org.junit.Test;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-02 15:33
 */
public class OtherTest
{

    interface A
    {
        String name();
    }

    class B
    {
        A a;
    }

    @Test
    public void test123() throws Exception
    {
        B b = new B();
        b.a = () ->
        {
            System.out.println(this + ",0");
            return "asd";
        };
        System.out.println(b.a + ",1");
        call(b);

    }

    private void call(B b)
    {
        System.out.println(b.a.name());
        System.out.println(b.a + ",2");
    }

}
