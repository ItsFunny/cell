package com.cell.services.impl;

import com.cell.services.ISayService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-11 22:19
 */
@DubboService(interfaceName = "asd")
public class SayServiceImpl implements ISayService
{
    @Override
    public String sayHelloByName(String name)
    {
        return "charlie_" + name;
    }
}
