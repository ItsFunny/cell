package com.cell.demo.http;

import com.cell.demo.common.ClientRequestDemo;
import com.cell.base.common.enums.EnumHttpRequestType;
import com.cell.base.common.utils.RandomUtils;
import com.cell.base.core.annotations.*;
import com.cell.base.core.concurrent.base.Future;
import com.cell.discovery.nacos.grpc.client.extension.server.IGRPCNacosClientServer;
import com.cell.http.framework.annotation.HttpCmdAnno;
import com.cell.http.framework.command.impl.AbstractHttpCommand;
import com.cell.http.framework.context.IHttpCommandContext;
import com.cell.http.framework.dispatcher.IHttpDispatcher;
import com.cell.http.framework.reactor.IMapDynamicHttpReactor;
import com.cell.http.framework.reactor.impl.AbstractHttpDymanicCommandReactor;
import com.cell.node.spring.annotation.CellSpringHttpApplication;
import com.cell.rpc.grpc.client.framework.server.ILocalGRPCClientServer;
import com.cell.runnable.spring.framework.all.CellSpringFrameworkApplication;
import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * @author Charlie
 * @When
 * @Description 混合的api接口实现
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-04 06:09
 */
@CellSpringHttpApplication
public class DemoApp
{
    public static class CC1
    {

    }

    @ActivePlugin
    public static class CC2
    {
        @Plugin
        public CC3 cc3()
        {
            return new CC3();
        }
    }

    public static class CC3
    {

    }

    @Bean
    public CC1 cc()
    {
        return new CC1();
    }


    @HttpCmdAnno(uri = "/cmd1", reactor = Reactor1.class)
    public static class Reactor1Cmd1 extends AbstractHttpCommand
    {
        @Override
        protected void onExecute(IHttpCommandContext ctx, Object bo) throws IOException
        {
            Reactor1 reactor1 = (Reactor1) ctx.getReactor();
            Assert.notNull(reactor1.logic, "asd");
            ctx.response(this.createResponseWp().ret("cmd1").build());
        }
    }

    @HttpCmdAnno(uri = "/reactor2cmd1", reactor = Reactor2.class)
    public static class Reactor2CMD1 extends AbstractHttpCommand
    {
        @Override
        protected void onExecute(IHttpCommandContext ctx, Object bo) throws IOException
        {
            ctx.response(this.createResponseWp().ret("reactor2#cmd1").build());
        }
    }

    @ActivePlugin
    public static class LogicImpl
    {

    }

    @ReactorAnno(group = "/reactor1")
    public static class Reactor1 extends AbstractHttpDymanicCommandReactor
    {
        @AutoPlugin
        private IHttpDispatcher commandDispatcher;
        @AutoPlugin
        private LogicImpl logic;
    }

    public static class Reactor2 extends AbstractHttpDymanicCommandReactor
    {
    }

    @ReactorAnno(group = "/reactor3")
    public static class Reactor3 extends AbstractHttpDymanicCommandReactor
    {
    }

    @Data
    public static class Cmd3Buz
    {
        private String name;
        @Optional
        private Integer age;
    }

    @HttpCmdAnno(uri = "/cmd3",
            buzzClz = Cmd3Buz.class,
            requestType = EnumHttpRequestType.HTTP_URL_GET, reactor = Reactor3.class)
    public static class cm3 extends AbstractHttpCommand
    {
        @Override
        protected void onExecute(IHttpCommandContext ctx, Object bo) throws IOException
        {
            Cmd3Buz c = (Cmd3Buz) bo;
            ctx.response(this.createResponseWp().ret(123).build());
        }
    }

    @HttpCmdAnno(uri = "/long", requestType = EnumHttpRequestType.HTTP_URL_GET)
    public static class LongCmd extends AbstractHttpCommand
    {

        @Override
        protected void onExecute(IHttpCommandContext ctx, Object o) throws IOException
        {
            try
            {
                TimeUnit.SECONDS.sleep(RandomUtils.randomInt(1, 10));
                ctx.response(this.createResponseWp().ret("done").build());
            } catch (Exception e)
            {

            }
        }
    }

    @ActivePlugin
    public static class RPCClient1
    {
//        @GRPCClient(value = "static://127.0.0.1:12000")
//        BaseGrpcGrpc.BaseGrpcStub stub;
    }

    @ReactorAnno()
    public static class RPCReactor extends AbstractHttpDymanicCommandReactor
    {
        @AutoPlugin
        private RPCClient1 client1;

        @AutoPlugin
        private ILocalGRPCClientServer im;
        @AutoPlugin
        private IGRPCNacosClientServer nacosClientServer;
    }


    @HttpCmdAnno(uri = "/localRpc", requestType = EnumHttpRequestType.HTTP_URL_GET, reactor = RPCReactor.class)
    public static class RpcCommand1 extends AbstractHttpCommand
    {
        @Override
        protected void onExecute(IHttpCommandContext ctx, Object o) throws IOException
        {
            RPCReactor reactor = (RPCReactor) ctx.getHttpReactor();
            ClientRequestDemo demo = new ClientRequestDemo();
            Future<Object> call = reactor.im.call(ctx, demo);
            try
            {
                Object o1 = call.get();
                ctx.response(this.createResponseWp().ret(o1).build());
            } catch (Exception e)
            {
                ctx.response(this.createResponseWp().exception(e).build());
            }
        }
    }

    @HttpCmdAnno(uri = "/rpc", requestType = EnumHttpRequestType.HTTP_URL_GET, reactor = RPCReactor.class)
    public static class RpcCommand2 extends AbstractHttpCommand
    {

        @Override
        protected void onExecute(IHttpCommandContext ctx, Object o) throws IOException
        {
            RPCReactor reactor = (RPCReactor) ctx.getHttpReactor();
            ClientRequestDemo demo = new ClientRequestDemo();
            Future<Object> call = reactor.nacosClientServer.call(ctx, demo);
            try
            {
                Object o1 = call.get();
                ctx.response(this.createResponseWp().ret(o1).build());
            } catch (Exception e)
            {
                ctx.response(this.createResponseWp().exception(e).ret(e.getMessage()).build());
            }
        }
    }

    public static void main(String[] args)
    {
        CellSpringFrameworkApplication.builder(DemoApp.class)
                .withReactor(new Reactor2())
                .newReactor()
                .withBean(CC1.class)
                .withBean(CC2.class)
                .withGroup("/demo")
                .post("/cc1", (wp) ->
                {
                    IMapDynamicHttpReactor reactor = wp.getReactor();
                    CC1 cc1 = (CC1) reactor.getDependency(CC1.class);
                    Assert.notNull(cc1, "bean cc1不可为空");
                    wp.success("post");
                }).make().get("/get", (wp) ->
        {
            wp.success("get");
        }).make().done().build().start(args);
    }
}
