package com.cell.component.httpsecurity.security.funs;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2022/4/11 3:51 下午
 */
public interface OnRequestReceived
{
    Authentication onRecv(HttpServletRequest request);

}
