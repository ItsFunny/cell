package com.cell.runnable.spring.framework.all;

import com.cell.base.core.annotations.ForceOverride;
import com.cell.base.core.annotations.ReactorAnno;
import com.cell.base.common.enums.EnumHttpRequestType;
import com.cell.base.common.enums.EnumHttpResponseType;
import com.cell.base.common.exceptions.ProgramaException;
import com.cell.base.common.utils.StringUtils;
import com.cell.base.core.protocol.ICommand;
import com.cell.http.framework.annotation.HttpCmdAnno;
import com.cell.http.framework.bo.BuzzContextBO;
import com.cell.http.framework.command.IBuzzExecutor;
import com.cell.http.framework.command.impl.AbsDeltaHttpCommand;
import com.cell.http.framework.reactor.IDynamicHttpReactor;
import com.cell.http.framework.reactor.IMapDynamicHttpReactor;
import com.cell.http.framework.reactor.impl.AbsMapHttpDynamicCommandReactor;
import com.cell.node.spring.annotation.CellSpringHttpApplication;
import com.cell.http.framework.reactor.IHttpReactor;
import com.cell.base.framework.root.Root;
import com.cell.http.framework.command.IHttpCommand;
import com.cell.base.core.utils.ClassUtil;
import com.cell.base.core.utils.ReflectUtil;
import lombok.Data;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.description.annotation.AnnotationDescription;
import net.bytebuddy.dynamic.loading.ByteArrayClassLoader;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.implementation.InvocationHandlerAdapter;
import net.bytebuddy.matcher.ElementMatchers;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-01 09:39
 */
@Data
public class CellSpringFrameworkApplication
{
    private SpringApplicationBuilder builder;
    private int port;

    public static ApplicationContext run(Class<?> clz, String[] args)
    {
        try
        {
            return builder(clz).build().start(args);
        } catch (Exception e)
        {
            throw new ProgramaException(e);
        }
    }

    // FIXME
    public ApplicationContext start(String[] args)
    {
        if (null == this.builder)
        {
            throw new ProgramaException("asd");
        }
        SpringApplication app = this.builder.build();

        Class<?> mainClz = ClassUtil.getMainApplicationClass();
        CellSpringHttpApplication annotation = mainClz.getAnnotation(CellSpringHttpApplication.class);
        Class<? extends Annotation>[] classes = annotation.scanInterestAnnotations();
        List<Class<? extends Annotation>> collect = Stream.of(classes).collect(Collectors.toList());
        collect.add(HttpCmdAnno.class);
        ReflectUtil.modify(mainClz, CellSpringHttpApplication.class, "scanInterestAnnotations", collect.toArray(new Class<?>[collect.size()]));

//        app.setWebApplicationType(WebApplicationType.REACTIVE);
        try
        {
            return app.run(args);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }


    public static CellSpringFrameworkApplicationBuilder builder(Class<?> clz)
    {
        CellSpringFrameworkApplicationBuilder ret = new CellSpringFrameworkApplicationBuilder(clz);
        return ret;
    }


    public static class CellSpringFrameworkApplicationBuilder
    {
        private List<IHttpReactor> reactors = new ArrayList<>();
        private List<ReactorBuilder> reactorBuilders = new ArrayList<>();
        private SpringApplicationBuilder applicationBuilder;
        private int port = 8080;
        private Class<?> clz;

        public CellSpringFrameworkApplicationBuilder(Class<?> clz)
        {
            this.clz = clz;
            this.applicationBuilder = new SpringApplicationBuilder(clz);
        }

        public CellSpringFrameworkApplicationBuilder()
        {
        }

        public CellSpringFrameworkApplicationBuilder properties(String... defaultProperties)
        {
            this.applicationBuilder = this.applicationBuilder.properties(defaultProperties);
            return this;
        }

        public CellSpringFrameworkApplicationBuilder withReactor(IHttpReactor reactor)
        {
            this.reactors.add(reactor);
            return this;
        }

        public CellSpringFrameworkApplicationBuilder withPort(int port)
        {
            this.port = port;
            return this;
        }

        public ReactorBuilder newReactor()
        {
            ReactorBuilder ret = new ReactorBuilder(this);
            this.reactorBuilders.add(ret);
            return ret;
        }

        public CellSpringFrameworkApplication build()
        {
            ByteBuddyAgent.install();

            for (ReactorBuilder builder : this.reactorBuilders)
            {
                try
                {
                    this.reactors.add(builder.build());
                } catch (Exception e)
                {
                    throw new ProgramaException(e);
                }
            }
            for (IHttpReactor reactor : reactors)
            {
                AnnotationAttributes attributes = ClassUtil.getMergedAnnotationAttributes(reactor.getClass(), ReactorAnno.class);
                if (attributes != null && !attributes.isEmpty())
                {
                    continue;
                }
                new ByteBuddy()
                        .redefine(reactor.getClass())
                        .annotateType(AnnotationDescription.Builder.ofType(ReactorAnno.class)
                                .define("group", "")
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
                                        return true;
                                    }
                                })
                                .build())
                        .make()
                        .load(Thread.currentThread().getContextClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
            }

            CellSpringFrameworkApplication ret = new CellSpringFrameworkApplication();
            ret.builder = this.applicationBuilder;
            ret.port = this.port;
            return ret;
        }
    }


    public static class ReactorBuilder
    {
        private CellSpringFrameworkApplicationBuilder CellSpringFrameworkApplicationBuilder;
        private List<HttpCommandBuilder> cmds = new ArrayList<>();
        private Set<Class<?>> dependencies = new HashSet<>();
        private Set<String> beanNames = new HashSet<>();
        private String group = "";
        private boolean forceOverride = false;

        public ReactorBuilder(CellSpringFrameworkApplicationBuilder builder)
        {
            this.CellSpringFrameworkApplicationBuilder = builder;
        }

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

        public ReactorBuilder withBeanName(String name)
        {
            this.beanNames.add(name);
            return this;
        }

        public CellSpringFrameworkApplicationBuilder done()
        {
            return this.CellSpringFrameworkApplicationBuilder;
        }

        public HttpCommandBuilder newCommand()
        {
            HttpCommandBuilder ret = new HttpCommandBuilder(this);
            this.cmds.add(ret);
            return ret;
        }

        public HttpCommandBuilder post(String uri, IBuzzExecutor bundle)
        {
            return this.newCommand()
                    .withUri(uri)
                    .withRequestType(EnumHttpRequestType.HTTP_POST)
                    .withBuzzHandler(bundle);
        }

        public HttpCommandBuilder get(String uri, IBuzzExecutor bundle)
        {
            return this.newCommand()
                    .withUri(uri)
                    .withRequestType(EnumHttpRequestType.HTTP_URL_GET)
                    .withBuzzHandler(bundle);
        }

        public IDynamicHttpReactor build() throws Exception
        {
            if (this.cmds.size() == 0)
            {
                throw new ProgramaException("cmd必须存在");
            }
            // FIXME OPTIMIZE
            IMapDynamicHttpReactor reactor = this.createDynamicHttpReactor();
            Root.getInstance().addReactor(reactor);
//            DefaultReactorHolder.addReactor(reactor);
            return reactor;
        }

        private IMapDynamicHttpReactor createDynamicHttpReactor() throws Exception
        {
            List<Class<? extends IHttpCommand>> cmdList = new ArrayList<>();

            for (HttpCommandBuilder cmd : cmds)
            {
                cmdList.add(cmd.build().getClass());
            }
            Class<?>[] classes = cmdList.toArray(new Class<?>[cmdList.size()]);
            IMapDynamicHttpReactor ret = new ByteBuddy()
                    .subclass(AbsMapHttpDynamicCommandReactor.class)
                    .implement(IMapDynamicHttpReactor.class)
                    .method(ElementMatchers.named("getDependencyList"))
                    .intercept(FixedValue.value(new HashSet<>(this.dependencies)))
                    .method(ElementMatchers.named("getDependencyListByName"))
                    .intercept(FixedValue.value(new HashSet<>(this.beanNames)))
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
                            .defineTypeArray("cmds", classes)
                            .build())
                    .make()
                    .load(this.getClass().getClassLoader())
                    .getLoaded()
                    .getDeclaredConstructor()
                    .newInstance();
            for (Class<?> aClass : classes)
            {
                ReflectUtil.modify(aClass, HttpCmdAnno.class, "reactor", ret.getClass());
            }
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
        private Class<?> buzzClz = Void.class;

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

        public HttpCommandBuilder withBuzzClz(Class<?> clz)
        {
            this.buzzClz = clz;
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

        private IHttpCommand build() throws Exception
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
                    .annotateType(AnnotationDescription.Builder.ofType(HttpCmdAnno.class)
                            .define("requestType", this.requestType)
                            .define("responseType", this.responseType)
                            .define("uri", this.uri)
                            .define("viewName", this.viewName)
//                            .define("httpCommandId", (short) commandId.getAndIncrement())
                            .define("buzzClz", this.buzzClz)
                            .build())
                    .make()
                    .load(this.getClass().getClassLoader(),ClassReloadingStrategy.fromInstalledAgent())
                    .getLoaded()
                    .getDeclaredConstructor()
                    .newInstance();
        }
    }

    public static class DEFAULT_DEMO_POST implements IBuzzExecutor
    {

        @Override
        public void execute(BuzzContextBO bo)
        {
            bo.success("post done ");
        }
    }

    public static class DEFAULT_DEMO_GET implements IBuzzExecutor
    {

        @Override
        public void execute(BuzzContextBO bo)
        {
            bo.success("get");
        }
    }

}
