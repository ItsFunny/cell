package com.cell.node.spring.postprocessors.extension;

import com.cell.base.common.constants.DebugConstants;
import com.cell.base.common.exceptions.ConfigException;
import com.cell.base.common.models.Module;
import com.cell.base.common.utils.IPUtils;
import com.cell.base.common.context.AbstractInitOnce;
import com.cell.base.common.context.InitCTX;
import com.cell.sdk.log.LOG;
import com.cell.node.core.configuration.RootConfiguration;
import com.cell.node.core.context.INodeContext;
import com.cell.node.core.exception.ExtensionImportException;
import com.cell.node.core.extension.INodeExtension;
import com.cell.node.spring.bridge.ISpringNodeExtension;
import com.cell.node.spring.context.SpringNodeContext;
import com.cell.node.spring.exception.ContainerException;
import com.cell.node.spring.postprocessor.ManagerFactoryPostProcessor;
import com.cell.node.spring.tools.Banner;
import com.cell.node.spring.utils.FrameworkUtil;
import com.cell.sdk.log.LogLevel;
import com.google.common.base.Stopwatch;
import org.apache.commons.cli.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.DefaultApplicationArguments;
import org.springframework.boot.context.event.*;
import org.springframework.context.ApplicationListener;

import java.util.*;
import java.util.concurrent.TimeUnit;


public class SpringExtensionManager extends AbstractInitOnce implements ApplicationListener<SpringApplicationEvent>, BeanPostProcessor
{
    private List<INodeExtension> extensions = new ArrayList<>();
    private Set<String> unimportedSet = new HashSet<>();
    private SpringNodeContext ctx;
    private Map<String, BeanDefinition> BeanDefinitionMap;
    private Map<Class<?>, BeanPostProcessor> processors = new HashMap<>();

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
                if (FrameworkUtil.checkIsExtension(gbdef.getBeanClass()))
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
                            ContainerException cex = new ContainerException(String.format("duplicated opt name [%s]", op.getOpt()));
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
        this.fillCtx();

        String[] alist = customArgs.toArray(new String[customArgs.size()]);
        SpringNodeContext dCtx = ctx;
        CommandLine commands = parser.parse(allOps, alist);
        dCtx.setCommandLine(commands);
        LOG.setLogLevel(LogLevel.INFO);
    }

    private void fillCtx()
    {
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

        Option option = allOps.getOption(DebugConstants.DOMAIN);
        if (option == null)
        {
            allOps.addOption(DebugConstants.DOMAIN, true, "公网域名");
        }
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException
    {
        BeanPostProcessor beanPostProcessor = this.processors.get(bean.getClass());
        if (null != beanPostProcessor)
        {
            return beanPostProcessor.postProcessBeforeInitialization(bean, beanName);
        }
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
    {
        boolean is = bean instanceof ISpringNodeExtension;
        if (!is)
        {
            return getAfterProcessor(bean, beanName);
        }
        this.initOnce(null);
        INodeExtension newEx = addExtension((INodeExtension) bean);
        try
        {
            if (!unimportedSet.contains(newEx.getName()))
            {
                Stopwatch started = Stopwatch.createStarted();
                Object o = newEx.loadConfiguration(ctx);
                if (o != null)
                {
                    RootConfiguration.getInstance().put(newEx.getClass(), o);
                }
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
        return getAfterProcessor(bean, beanName);
    }

    private Object getAfterProcessor(Object bean, String beanName)
    {
        BeanPostProcessor beanPostProcessor = this.processors.get(bean.getClass());
        if (null != beanPostProcessor)
        {
            return beanPostProcessor.postProcessAfterInitialization(bean, beanName);
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
                ManagerFactoryPostProcessor.getInstance().finalHandleManager();
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
            } else
            {
                LOG.warn(Module.CONTAINER, "application ready twice");
            }
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

    public Map<Class<?>, BeanPostProcessor> getProcessors()
    {
        return processors;
    }

    public void setProcessors(Map<Class<?>, BeanPostProcessor> processors)
    {
        this.processors = processors;
    }
}
