package com.cell.protocol;

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
    void setStatus(long sc);
    void addHeader(String name, String value);
    void fireResult(Object o);
    boolean isSetOrExpired();
}
