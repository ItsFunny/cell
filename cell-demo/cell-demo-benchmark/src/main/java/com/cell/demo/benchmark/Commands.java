package com.cell.demo.benchmark;

import com.cell.base.common.enums.EnumHttpRequestType;
import com.cell.base.core.utils.ClassUtil;
import com.cell.http.framework.annotation.HttpCmdAnno;
import com.cell.http.framework.command.impl.AbstractHttpCommand;
import com.cell.http.framework.context.IHttpCommandContext;
import lombok.Data;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class Commands
{
    public static abstract class BaseCommand extends AbstractHttpCommand
    {
        private String path;

        public BaseCommand()
        {
            HttpCmdAnno anno = (HttpCmdAnno) ClassUtil.getAnnotation(this.getClass(), HttpCmdAnno.class);
            this.path = anno.uri();
        }

        @Override
        protected void onExecute(IHttpCommandContext ctx, Object o) throws IOException
        {
            ctx.response(this.createResponseWp().ret(this.path).build());
        }
    }

    @HttpCmdAnno(uri = "/bench/1", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command1 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/2", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command2 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/3", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command3 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/4", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command4 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/5", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command5 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/6", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command6 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/7", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command7 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/8", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command8 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/9", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command9 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/10", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command10 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/11", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command11 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/12", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command12 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/13", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command13 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/14", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command14 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/15", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command15 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/16", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command16 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/17", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command17 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/18", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command18 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/19", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command19 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/20", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command20 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/21", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command21 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/22", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command22 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/23", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command23 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/24", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command24 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/25", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command25 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/26", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command26 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/27", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command27 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/28", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command28 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/29", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command29 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/30", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command30 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/31", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command31 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/32", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command32 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/33", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command33 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/34", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command34 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/35", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command35 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/36", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command36 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/37", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command37 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/38", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command38 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/39", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command39 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/40", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command40 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/41", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command41 extends BaseCommand {}


}
