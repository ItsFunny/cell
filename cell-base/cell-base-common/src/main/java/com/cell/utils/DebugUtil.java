package com.cell.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.PrintWriter;
import java.io.StringWriter;


/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2020-04-08 10:38
 */
@Slf4j
public class DebugUtil
{


    public static void infoPrint(String msg, Object obj)
    {
        StringBuilder sb=new StringBuilder();
        sb.append("<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<< \n");
        sb.append( "当前线程:" + Thread.currentThread().getId() + "," );
        if (msg!=null){
            sb.append(msg);
        }
        if (obj instanceof String){
            sb.append(obj);
        }else {
            sb.append(JSONUtil.toFormattedJson(obj));
        }
        sb.append("\n");
        sb.append(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        log.info(sb.toString());
    }
    public static void infoPrint(Object obj)
    {
        infoPrint(null,obj);
    }


    public static String exceptionStackTraceToString(Throwable throwable)
    {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
}
