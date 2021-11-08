package com.cell.base.core.protocol;

import com.cell.base.core.concurrent.base.Promise;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-10-22 04:48
 */
public interface IServerResponse
{
    void setHeader(String name, String value);

    void setPromise(Promise<Object> promise);

    Promise<Object> getPromise();

    void setStatus(long sc);

    void addHeader(String name, String value);

    Object getResponse();

    void fireResult(Object o);

    void fireFailure(Exception e);

    boolean isSetOrExpired();
}
