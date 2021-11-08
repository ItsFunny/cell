package com.cell.log.impl;

import com.cell.sdk.log.LOG;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-07-27 08:52
 */
public class OtherTest
{
    static Logger logger = LoggerFactory.getLogger("123");

    static
    {
        LOG.info("xxx");
    }

    @Test
    public void testOther()
    {
        LOG.info("123");
        Logger logger2 = LoggerFactory.getLogger("444444");
        OtherTest.logger.info("info");
        logger2.info("4444444");
    }

}
