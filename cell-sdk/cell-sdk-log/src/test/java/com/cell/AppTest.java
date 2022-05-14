package com.cell;

import com.cell.base.common.models.ModuleInterface;
import com.cell.sdk.log.LOG;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    enum  A implements  ModuleInterface{
        VV()
        ;

        @Override
        public short getModuleId()
        {
            return 1;
        }
    }
    @Test
    public void logTest()
    {
        LOG.info(A.VV,"asd");
    }

    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue(true);
    }
}
