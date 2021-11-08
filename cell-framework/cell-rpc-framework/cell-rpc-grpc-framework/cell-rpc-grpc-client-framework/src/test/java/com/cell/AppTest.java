package com.cell;

import com.cell.base.core.concurrent.DummyExecutor;
import com.cell.base.core.concurrent.base.Promise;
import com.cell.base.core.concurrent.promise.BaseDefaultPromise;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue(true);
    }

    @Test
    public void test123() throws Exception
    {
        Promise<Object> promise = new BaseDefaultPromise(DummyExecutor.getInstance());
        Object o = promise.get();
        System.out.println(o);
    }
}
