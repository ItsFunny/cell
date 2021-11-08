package com.cell.log.impl;

import net.bytebuddy.ByteBuddy;
import org.junit.Test;
import org.slf4j.LoggerFactory;
import org.slf4j.impl.StaticLoggerBinder;


/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-07-22 21:31
 */
public class ByteBuddyTest
{
    static
    {

    }

    public static void init()
    {

    }

    public static void testStcic() throws Exception
    {
        Class<? extends StaticLoggerBinder> loaded = new ByteBuddy()
                .subclass(StaticLoggerBinder.class)
                .make()
                .load(ByteBuddyTest.class.getClassLoader())
                .getLoaded();
        StaticLoggerBinder staticLoggerBinder = loaded.newInstance();
        System.out.println(staticLoggerBinder.getLoggerFactory());
        System.out.println(staticLoggerBinder);
    }

    @Test
    public void testReplace()
    {
        LoggerFactory.getLogger("");
    }


    public static void main(String[] args) throws Exception
    {
        testStcic();
//        Class<?> dynamicType = new ByteBuddy()
//                .subclass(Object.class)
//                .method(ElementMatchers.named("toString"))
//                .intercept(FixedValue.value("asddddd!"))
//                .make()
//                .load(ByteBuddyTest.class.getClassLoader())
//                .getLoaded();
//        String s = dynamicType.newInstance().toString();
//        System.out.println(s);
//        assertThat(s, is("Hello World!"));
    }
}
