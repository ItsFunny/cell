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


    @HttpCmdAnno(uri = "/bench/42", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command42 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/43", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command43 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/44", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command44 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/45", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command45 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/46", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command46 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/47", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command47 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/48", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command48 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/49", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command49 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/50", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command50 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/51", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command51 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/52", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command52 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/53", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command53 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/54", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command54 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/55", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command55 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/56", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command56 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/57", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command57 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/58", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command58 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/59", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command59 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/60", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command60 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/61", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command61 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/62", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command62 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/63", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command63 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/64", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command64 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/65", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command65 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/66", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command66 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/67", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command67 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/68", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command68 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/69", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command69 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/70", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command70 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/71", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command71 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/72", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command72 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/73", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command73 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/74", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command74 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/75", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command75 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/76", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command76 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/77", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command77 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/78", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command78 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/79", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command79 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/80", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command80 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/81", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command81 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/82", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command82 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/83", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command83 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/84", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command84 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/85", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command85 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/86", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command86 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/87", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command87 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/88", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command88 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/89", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command89 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/90", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command90 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/91", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command91 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/92", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command92 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/93", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command93 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/94", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command94 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/95", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command95 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/96", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command96 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/97", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command97 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/98", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command98 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/99", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command99 extends BaseCommand {}

    @HttpCmdAnno(uri = "/bench/100", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class Command100 extends BaseCommand {}
}
