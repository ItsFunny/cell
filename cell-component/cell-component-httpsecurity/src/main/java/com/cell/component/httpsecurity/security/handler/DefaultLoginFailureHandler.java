package com.cell.component.httpsecurity.security.handler;

import com.mi.wallet.mange.context.IExceptionHandler;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2022/4/11 2:16 下午
 */
public class DefaultLoginFailureHandler extends DefaultFailHandler implements LoginFailHandler
{

    public DefaultLoginFailureHandler(IExceptionHandler exceptionHandler)
    {
        super(exceptionHandler);
    }
}
