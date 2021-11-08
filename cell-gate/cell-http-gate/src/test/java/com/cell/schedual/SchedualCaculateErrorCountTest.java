package com.cell.schedual;

import org.junit.Test;

public class SchedualCaculateErrorCountTest
{

    @Test
    public void testNormal() throws Exception
    {
        SchedualCaculateErrorCount counter = new SchedualCaculateErrorCount(10, 100L, 0.01D, "错误应答数超过限制");
        counter.start();
        int count = 1000;
        new Thread(() ->
        {
            for (int i = 0; i < count * 5; i++)
            {
                counter.incTotal();
                try
                {
                    Thread.sleep(1);
                } catch (InterruptedException e)
                {
                }
            }
        }).start();
        new Thread(() ->
        {
            for (int i = 0; i < count; i++)
            {
                counter.incAndGet();
                try
                {
                    Thread.sleep(5);
                } catch (InterruptedException e)
                {
                }
            }
        }).start();
        ;

        Thread.sleep(5000L);
        counter.stop();
    }

}