package com.cell.factory;

import com.cell.annotations.Command;
import com.cell.annotations.ForceOverride;
import com.cell.annotations.HttpCmdAnno;
import com.cell.annotations.ReactorAnno;
import com.cell.command.IHttpCommand;
import com.cell.command.impl.AbsDeltaHttpCommand;
import com.cell.command.IBuzzExecutor;
import com.cell.dispatcher.DefaultReactorHolder;
import com.cell.enums.EnumHttpRequestType;
import com.cell.enums.EnumHttpResponseType;
import com.cell.exceptions.ProgramaException;
import com.cell.postprocessor.ReactorCache;
import com.cell.reactor.IMapDynamicHttpReactor;
import com.cell.reactor.IMapDynamicHttpReactor;
import com.cell.reactor.impl.AbstractHttpDymanicCommandReactor;
import com.cell.utils.StringUtils;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;
import org.springframework.scheduling.concurrent.ScheduledExecutorTask;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-02 10:31
 */
public class ReactoryFactory
{
    private static final AtomicLong commandId = new AtomicLong(1);

    public static ReactorBuilder builder()
    {
        return new ReactorBuilder();
    }

    public static class ReactorBuilder
    {
        private List<HttpCommandBuilder> cmds = new ArrayList<>();
        private Set<Class<?>> dependencies = new HashSet<>();
        private String group = "";
        private boolean forceOverride = false;

        public ReactorBuilder withGroup(String group)
        {
            this.group = group;
            return this;
        }

        public ReactorBuilder withBean(Class<?> clz)
        {
            this.dependencies.add(clz);
            return this;
        }

        public HttpCommandBuilder newCommand()
        {
            HttpCommandBuilder ret = new HttpCommandBuilder(this);
            this.cmds.add(ret);
            return ret;
        }

        public IMapDynamicHttpReactor build() throws Exception
        {
            if (this.cmds.size() == 0)
            {
                throw new ProgramaException("cmd必须存在");
            }
            IMapDynamicHttpReactor reactor = this.createDynamicHttpReactor();
            DefaultReactorHolder.addReactor(reactor);
            return reactor;
        }

        private IMapDynamicHttpReactor createDynamicHttpReactor() throws Exception
        {
            List<Class<? extends IHttpCommand>> cmdList = new ArrayList<>();

            for (HttpCommandBuilder cmd : cmds)
            {
                cmdList.add(cmd.build().getClass());
            }
            IMapDynamicHttpReactor ret = (IMapDynamicHttpReactor) new ByteBuddy()
                    .subclass(AbstractHttpDymanicCommandReactor.class)
                    .implement(IMapDynamicHttpReactor.class)
                    .method(ElementMatchers.named("getHttpCommandList"))
                    .intercept(FixedValue.value(new ArrayList<>(cmdList)))
                    .method(ElementMatchers.named("getDependencyList"))
                    .intercept(FixedValue.value(new HashSet<>(this.dependencies)))
                    .annotateType(AnnotationDescription.Builder.ofType(ReactorAnno.class)
                            .define("group", this.group)
                            .define("withForce", new ForceOverride()
                            {
                                @Override
                                public Class<? extends Annotation> annotationType()
                                {
                                    return ForceOverride.class;
                                }

                                @Override
                                public boolean forceOverride()
                                {
                                    return forceOverride;
                                }
                            })
                            .build())
                    .make()
                    .load(this.getClass().getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor()
                    .newInstance();

            return ret;
        }
    }


    public static class HttpCommandBuilder
    {
        private ReactorBuilder reactorBuilder;
        private String uri;
        private EnumHttpRequestType requestType = EnumHttpRequestType.HTTP_POST;
        private EnumHttpResponseType responseType = EnumHttpResponseType.HTTP_JSON;
        private IBuzzExecutor buzzExecutor;
        private String viewName = "";

        public HttpCommandBuilder(ReactorBuilder reactorBuilder)
        {
            this.reactorBuilder = reactorBuilder;
        }

        public HttpCommandBuilder withViewName(String viewName)
        {
            this.viewName = viewName;
            return this;
        }

        public HttpCommandBuilder withBuzzHandler(IBuzzExecutor bundle)
        {
            this.buzzExecutor = bundle;
            return this;
        }

        public HttpCommandBuilder newCommand()
        {
            return this.reactorBuilder.newCommand();
        }

        public HttpCommandBuilder withUri(String uri)
        {
            this.uri = uri;
            return this;
        }

        public HttpCommandBuilder withRequestType(EnumHttpRequestType requestType)
        {
            this.requestType = requestType;
            return this;
        }

        public HttpCommandBuilder withResponseType(EnumHttpResponseType responseType)
        {
            this.responseType = responseType;
            return this;
        }

        public ReactorBuilder make()
        {
            return this.reactorBuilder;
        }

        public IHttpCommand build() throws Exception
        {
            if (StringUtils.isEmpty(this.uri))
            {
                // FIXME
                throw new ProgramaException("uri为空");
            }
            if (this.buzzExecutor == null)
            {
                throw new ProgramaException("jsonHandler为空");
            }
            return this.createByteBuddyCommand();
        }

        private IHttpCommand createByteBuddyCommand() throws Exception
        {
            Class<? extends IHttpCommand> clz = null;
            String methodName = null;
            if (this.responseType == EnumHttpResponseType.HTTP_JSON)
            {
                clz = AbsDeltaHttpCommand.class;
                methodName = "setUpBuzzExecutor";
            }
            return new ByteBuddy()
                    .subclass(clz)
                    .method(ElementMatchers.named(methodName))
                    .intercept(InvocationHandlerAdapter.of((o, proxy, args) ->
                            this.buzzExecutor))
                    .annotateType(AnnotationDescription.Builder.ofType(Command.class).define("commandId", (short) commandId.getAndIncrement()).build())
                    .annotateType(AnnotationDescription.Builder.ofType(HttpCmdAnno.class)
                            .define("requestType", this.requestType)
                            .define("responseType", this.responseType)
                            .define("uri", this.uri)
                            .define("viewName", this.viewName)
                            .build())
                    .make()
                    .load(ReactoryFactory.class.getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor()
                    .newInstance();
        }
    }


    static class SingleJsonInvocationHandler implements InvocationHandler
    {
        public SingleJsonInvocationHandler(IBuzzExecutor bundle)
        {
            this.bundle = bundle;
        }

        private IBuzzExecutor bundle;

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
        {
            Object ret = method.invoke(bundle, args);
            return ret;
        }
    }

    public static class SingleDynamicReactor extends AbstractHttpDymanicCommandReactor
    {
        private List<Class<? extends IHttpCommand>> cmds = new ArrayList<>(1);

        @Override
        public List<Class<? extends IHttpCommand>> getHttpCommandList()
        {
            return this.cmds;
        }
    }


}
