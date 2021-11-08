package com.cell.http.framework.adapter;

import javax.xml.bind.JAXB;
import java.io.StringWriter;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-02 05:28
 */
public class XMLHandlerMethodReturnValuleHandler<T> implements HandlerMethodReturnValueHandler<T>
{
    @Override
    public T handler(T t)
    {
        StringWriter sw = new StringWriter();
        JAXB.marshal(t, sw);
        String xmlString = sw.toString();
        return (T) xmlString;
    }
}
