package com.cell;

import com.cell.base.common.models.ModuleInterface;
import com.cell.sdk.log.LOG;
import com.cell.sdk.log.LogLevel;
import com.cell.sdk.log.constants.LogConstant;
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
        LOG.trace("trace:{}","trace");
        LOG.info(A.VV,"info:{}","info");
        LOG.warn(A.VV,"warn:{}","warn");
        LOG.erroring(A.VV,"err:{}","err");
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
