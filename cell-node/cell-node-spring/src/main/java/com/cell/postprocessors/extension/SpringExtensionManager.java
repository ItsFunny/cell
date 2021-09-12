package com.cell.postprocessors.extension;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.cell.annotations.ManagerNode;
import com.cell.bridge.ISpringNodeExtension;
import com.cell.comparators.InstanceOrderComparator;
import com.cell.comparators.OrderComparator;
import com.cell.config.AbstractInitOnce;
import com.cell.constants.Constants;
import com.cell.constants.OrderConstants;
import com.cell.context.INodeContext;
import com.cell.context.InitCTX;
import com.cell.context.SpringNodeContext;
import com.cell.exception.ContainerException;
import com.cell.exception.ExtensionImportException;
import com.cell.exceptions.ConfigException;
import com.cell.extension.INodeExtension;
import com.cell.log.LOG;
import com.cell.log.LogLevel;
import com.cell.manager.IReflectManager;
import com.cell.models.Module;
import com.cell.tool.Banner;
import com.cell.utils.CollectionUtils;
import com.cell.utils.DateUtils;
import com.cell.utils.IPUtils;
import com.cell.wrapper.AnnotaionManagerWrapper;
import com.cell.wrapper.AnnotationNodeWrapper;
import com.google.common.base.Stopwatch;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.lucene.util.CollectionUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.boot.context.event.SpringApplicationEvent;
import org.springframework.context.ApplicationListener;


public class SpringExtensionManager extends AbstractInitOnce implements ApplicationListener<SpringApplicationEvent>, BeanPostProcessor
{
    private List<INodeExtension> extensions = new ArrayList<>();
    private Set<String> unimportedSet = new HashSet<>();
    private SpringNodeContext ctx;
    private Map<String, BeanDefinition> BeanDefinitionMap;
    private Set<Class<?>> nodesSet;
    private Map<String, AnnotaionManagerWrapper> managers;

    private Options allOps;
    private int state = 0;
    private KillThread killThread = new KillThread();

    private static SpringExtensionManager SINGLETON = new SpringExtensionManager();

    public static SpringExtensionManager getInstance()
    {
        return SINGLETON;
    }

    private SpringExtensionManager()
    {
        ctx = new SpringNodeContext(this);
    }


    public boolean isExclusive(String extensionName)
    {
        return this.unimportedSet.contains(extensionName);
    }

    public INodeExtension addExtension(INodeExtension e)
    {
        extensions.add(e);
        return e;
    }

    public void setBeanDefinitionMap(Map<String, BeanDefinition> beanDefinitionMap)
    {
        BeanDefinitionMap = beanDefinitionMap;
    }

    public void setManagers(Map<String, AnnotaionManagerWrapper> managers)
    {
        this.nodesSet = new HashSet<>();
        Collection<AnnotaionManagerWrapper> values = managers.values();
        for (AnnotaionManagerWrapper value : values)
        {
            Map<String, AnnotationNodeWrapper> managerNodes = value.getManagerNodes();
            Collection<AnnotationNodeWrapper> nodes = managerNodes.values();
            for (AnnotationNodeWrapper node : nodes)
            {
                this.nodesSet.add(node.getClass());
            }
        }
        this.managers = managers;
    }


    void initCommandLine() throws ContainerException, ParseException, InstantiationException, IllegalAccessException, IllegalStateException
    {
        LOG.setLogLevel(LogLevel.DEBUG);
        DefaultApplicationArguments SpringCommand = new DefaultApplicationArguments(ctx.getArgs());
        List<String> customArgs = new ArrayList<>(SpringCommand.getNonOptionArgs());
        Set<String> springArgs = SpringCommand.getOptionNames();
        LOG.info(Module.CONTAINER, "spring args list: {}", springArgs);

        CommandLineParser parser = new DefaultParser();
        allOps = new Options();

        for (BeanDefinition def : BeanDefinitionMap.values())
        {
            if (def instanceof GenericBeanDefinition)
            {
                GenericBeanDefinition gbdef = (GenericBeanDefinition) def;
                if (ISpringNodeExtension.class.isAssignableFrom(gbdef.getBeanClass()))
                {
                    // FIXME factory proxy
                    ISpringNodeExtension ex = (ISpringNodeExtension) gbdef.getBeanClass().newInstance();
                    Options ops = ex.getOptions();
                    if (ops == null)
                    {
                        continue;
                    }
                    for (Option op : ops.getOptions())
                    {
                        if (allOps.hasOption(op.getOpt()))
                        {
                            ContainerException cex = new ContainerException(String.format("duplicated opt name [{}]", op.getOpt()));
                            LOG.error(Module.CONTAINER, cex, "extension {} have duplicated arg opt {}", gbdef.getBeanClass(), op.getOpt());
                            throw cex;
                        } else
                        {
                            allOps.addOption(op);
                        }
                    }
                }
            }
        }
        Option ip = allOps.getOption("ip");
        if (ip != null)
        {
            if (IPUtils.isIPv4(ip.getValue()))
            {
                throw new ConfigException("argument failure,illegal ip");
            }
            ctx.setIp(ip.getValue());
        } else
        {
            ctx.setIp(IPUtils.getLocalAddress());
        }
        String[] alist = customArgs.toArray(new String[customArgs.size()]);
        SpringNodeContext dCtx = ctx;
        CommandLine commands = parser.parse(allOps, alist);
        dCtx.setCommandLine(commands);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException
    {
        this.replace(bean, beanName);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
    {
        boolean is = bean instanceof ISpringNodeExtension;
        if (!is)
        {
            return bean;
        }
        this.initOnce(null);
        INodeExtension newEx = addExtension((INodeExtension) bean);
        try
        {
            if (!unimportedSet.contains(newEx.getName()))
            {
                Stopwatch started = Stopwatch.createStarted();
                newEx.init(ctx);
                started.stop();
                long elapsed = started.elapsed(TimeUnit.SECONDS);
                LOG.info(Module.CONTAINER, "extension init success, extension = {}, costTime = {}", newEx.getName(), elapsed);
            }
        } catch (Throwable e)
        {
            if (newEx.isRequired())
            {
                LOG.error(Module.CONTAINER, e, "extension {} init fail", newEx);
                throw new FatalBeanException(String.format("init extension {} fail", newEx), e);
            } else
            {
                unimportedSet.add(newEx.getName());
                LOG.error(Module.CONTAINER, e, "extension {} init fail and stop to import it", newEx);
            }
        }
        return bean;
    }

    @Override
    public void onApplicationEvent(SpringApplicationEvent event)
    {
        if (event instanceof ApplicationEnvironmentPreparedEvent)
        {
            if (state == 0)
            {
                state = 1;
            } else
            {
                LOG.warn(Module.CONTAINER, "environment prepared twice");
            }
        } else if (event instanceof ApplicationPreparedEvent)
        {
            if (state == 1)
            {
                initContext((ApplicationPreparedEvent) event);
                LOG.info(Module.CONTAINER, "\n{}", Banner.INIT);
                state = 2;
            } else
            {
                LOG.warn(Module.CONTAINER, "application int twice");
            }
        } else if (event instanceof ApplicationStartedEvent)
        {
            if (state == 2)
            {
                this.finalHandleManager();
                onStart((ApplicationStartedEvent) event);
                LOG.info(Module.CONTAINER, "\n{}", Banner.START);
                state = 3;
            } else
            {
                LOG.warn(Module.CONTAINER, "application start twice");
            }
        } else if (event instanceof ApplicationReadyEvent)
        {
            if (state == 3)
            {
                onReady((ApplicationReadyEvent) event);
                LOG.warn(Module.CONTAINER, "\n{}", Banner.菩萨保佑);
                LOG.info(Module.CONTAINER, "this jar arg list: {}", allOps);
                for (INodeExtension e : extensions)
                {
                    if (!unimportedSet.contains(e.getName()))
                    {
                        LOG.info(Module.CONTAINER, "successfully load extension {}", e.getClass().getName());
                    }
                }
                state = 4;
                this.flush();
            } else
            {
                LOG.warn(Module.CONTAINER, "application ready twice");
            }
        }
    }


    private void finalHandleManager()
    {
        Set<String> keys = managers.keySet();
        for (String key : keys)
        {
            AnnotaionManagerWrapper wrapper = managers.get(key);
            IReflectManager manager = wrapper.getManager();
            List<Object> collect = wrapper.getManagerNodes().values().stream().map(p -> p.getNode()).collect(Collectors.toList());
            Collections.sort(collect, (a, b) ->
            {
                ManagerNode n1 = a.getClass().getAnnotation(ManagerNode.class);
                ManagerNode n2 = b.getClass().getAnnotation(ManagerNode.class);
                int value1 = n1 == null ? OrderConstants.DEFAULT_ORDER : n1.orderValue();
                int value2 = n2 == null ? OrderConstants.DEFAULT_ORDER : n2.orderValue();
                return Integer.compare(value1, value2);
            });
            manager.invokeInterestNodes(collect);
        }
    }

    void addExcludeExtension(String eName)
    {
        unimportedSet.add(eName);
        LOG.info(Module.CONTAINER, "extension {} is adding to exclusive list", eName);
    }


    void initContext(ApplicationPreparedEvent event)
    {
        ctx.getApp().setEnvironment((event.getApplicationContext().getEnvironment()));
        ctx.getApp().setApp(event.getSpringApplication());
        ctx.setArgs(event.getArgs());
        ctx.setExtensions(extensions);
        ctx.getApp().setAppContext(event.getApplicationContext());
        event.getApplicationContext().getBeanFactory().addBeanPostProcessor(this);
    }

    void initBanner()
    {

    }


    public void onClose(INodeContext ctx)
    {
        close(ctx, extensions);
    }


    private void onStart(ApplicationStartedEvent event)
    {
        start(ctx);
        Runtime.getRuntime().addShutdownHook(killThread);
    }

    private void onReady(ApplicationReadyEvent event)
    {
        ready(ctx);
    }


    void start(INodeContext ctx)
    {
        final List<ExtensionCostTime> costTimeList = new ArrayList<>();
        for (INodeExtension extension : extensions)
        {
            try
            {
                if (!unimportedSet.contains(extension.getName()))
                {
                    final long startTime = System.currentTimeMillis();
                    extension.start(ctx);
                    final long costTime = System.currentTimeMillis() - startTime;
                    costTimeList.add(new ExtensionCostTime(extension, costTime));
                }
            } catch (Throwable e)
            {
                if (extension.isRequired())
                {
                    LOG.error(Module.CONTAINER, e, "extension {} start fail", extension);
                    throw new ExtensionImportException(String.format("start extension %s fail", extension), e);
                } else
                {
                    addExcludeExtension(extension.getName());
                    LOG.error(Module.CONTAINER, e, "extension {} start fail and stop to import it", extension);
                }
            }
        }

        costTimeList.sort(new Comparator<ExtensionCostTime>()
        {
            @Override
            public int compare(ExtensionCostTime o1, ExtensionCostTime o2)
            {
                if (o1.costTime > o2.costTime)
                {
                    return 1;
                } else if (o1.costTime < o2.costTime)
                {
                    return -1;
                } else
                {
                    return 0;
                }
            }
        });
        for (ExtensionCostTime extensionCostTime : costTimeList)
        {
            LOG.info(Module.CONTAINER, "extension start success, extension = {}, costTime = {}", extensionCostTime.extension, extensionCostTime.costTime);
        }
    }

    @Override
    protected void onInit(InitCTX ctx)
    {
        try
        {
            initCommandLine();
        } catch (Exception e)
        {
            LOG.error(Module.CONTAINER, e, "init command line fail");
            throw new FatalBeanException("init command line fail", e);
        }
    }

    private static class ExtensionCostTime
    {
        INodeExtension extension;
        long costTime;

        ExtensionCostTime(INodeExtension extension, long costTime)
        {
            this.extension = extension;
            this.costTime = costTime;
        }
    }

    void ready(INodeContext ctx)
    {
        int index = 0;
        for (INodeExtension extension : extensions)
        {
            try
            {
                if (!unimportedSet.contains(extension.getName()))
                {
                    extension.ready(ctx);
                }
            } catch (Throwable e)
            {
                if (extension.isRequired())
                {
//                    LOG.error(Module.CONTAINER, e, "extension {} start fail", extension);
                    List<INodeExtension> toCloseList = new ArrayList<>();
                    for (int i = 0; i < index; ++i)
                    {
                        toCloseList.add(this.extensions.get(i));
                    }
                    close(ctx, toCloseList);
                    throw new ExtensionImportException(String.format("start extension {} fail", extension), e);
                } else
                {
//                    LOG.error(Module.CONTAINER, e, "extension {} start fail and stop to import it", extension);
                }
            }
            ++index;
        }
    }

    void close(INodeContext ctx, List<INodeExtension> extensions)
    {
        for (INodeExtension extension : extensions)
        {
            try
            {
                if (!unimportedSet.contains(extension.getName()))
                {
                    extension.close(ctx);
                }
            } catch (Throwable e)
            {
//                LOG.error(Module.CONTAINER, e, "extension {} close fail", extension);
            }
        }
    }

    class KillThread extends Thread
    {

        @Override
        public void run()
        {
//            LOG.info(Module.CONTAINER, "node shutdown by signal");
            onClose(ctx);
        }
    }

    private void replace(Object bean, String beanName)
    {
        if (CollectionUtils.isEmpty(this.nodesSet))
        {
            return;
        }
        if (!this.nodesSet.contains(bean.getClass()))
        {
            return;
        }
        Set<String> keys = this.managers.keySet();
        for (String key : keys)
        {
            AnnotaionManagerWrapper annotaionManagerWrapper = this.managers.get(key);
            Map<String, AnnotationNodeWrapper> managerNodes = annotaionManagerWrapper.getManagerNodes();
            Map<String, AnnotationNodeWrapper> nodes = annotaionManagerWrapper.getManagerNodes();
            Collection<AnnotationNodeWrapper> values = nodes.values();
            for (AnnotationNodeWrapper value : values)
            {
                if (value.getNode() != null && value.getNode().getClass().equals(bean.getClass()))
                {
                    LOG.info(Module.CONTAINER, "bean 替换,origin:{},after:{},name:{}", value.getNode(), bean, beanName);
                    value.setNode(bean);
                }
            }
        }
    }

    private void flush()
    {
        this.nodesSet = null;
    }

}
