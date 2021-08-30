package com.cell.command;

import com.cell.enums.EnumHttpRequestType;
import com.cell.enums.EnumHttpResponseType;
import com.cell.protocol.ICommand;
import com.cell.reactor.IHttpReactor;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-28 11:04
 */
public interface IHttpCommand extends ICommand
{
    EnumHttpRequestType getRequestType();

    EnumHttpResponseType getResponseType();

    // 是否是一体化的那种模式 如果不为空,返回的是modelAndView
    String modelAndView();


    void setReactor(IHttpReactor reactor);

    IHttpReactor getReactor();
}
