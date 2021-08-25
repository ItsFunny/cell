package com.cell.bridge;

import java.util.*;

import com.cell.context.INodeContext;
import com.cell.context.SpringNodeContext;
import com.cell.exception.ContainerException;
import com.cell.exception.ExtensionImportException;
import com.cell.extension.INodeExtension;
import com.cell.log.LOG;
import com.cell.log.LogLevel;
import com.cell.models.Module;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
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


public class SpringExtensionManager implements ApplicationListener<SpringApplicationEvent>, BeanPostProcessor
{

    private List<INodeExtension> extensions = new ArrayList<>();
    private Set<String> unimportedSet = new HashSet<>();
    private SpringNodeContext ctx;
    private Map<String, BeanDefinition> BeanDefinitionMap;
    private boolean isInit = false;
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
        List<String> customArgs = SpringCommand.getNonOptionArgs();
        Set<String> springArgs = SpringCommand.getOptionNames();
        LOG.info(Module.CONTAINER, "spring args list: {}", springArgs);
        String[] alist = customArgs.toArray(new String[customArgs.size()]);
        CommandLineParser parser = new DefaultParser();
        allOps = new Options();
        for (BeanDefinition def : BeanDefinitionMap.values())
        {
            if (def instanceof GenericBeanDefinition)
            {
                GenericBeanDefinition gbdef = (GenericBeanDefinition) def;
                if (ISpringNodeExtension.class.isAssignableFrom(gbdef.getBeanClass()))
                {
                    ISpringNodeExtension ex = (ISpringNodeExtension) gbdef.getBeanClass().newInstance();
                    Options ops = ex.getOptions();
                    if (ops != null)
                    {
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
        }
        SpringNodeContext dCtx = (SpringNodeContext) ctx;
//		alist = addReg(alist);
        CommandLine commands = parser.parse(allOps, alist);
        dCtx.setCommandLine(commands);
    }

    private String[] addReg(String[] alist)
    {
        String[] defaultReg = new String[]{"-reg", "lilei1234546", "-cluster", "lilei1234546"};
        for (String cmd : alist)
        {
            if (cmd.equals("-reg"))
            {
                return alist;
            }
        }

        int len = alist.length + 2;
        String[] result = new String[len];
        System.arraycopy(alist, 0, result, 0, alist.length);
        System.arraycopy(defaultReg, 0, result, alist.length, 2);
        return result;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
    {
        if (bean instanceof INodeExtension)
        {
            if (!isInit)
            {
                try
                {
                    initCommandLine();
                } catch (Throwable e)
                {
//                    LOG.error(BSModule.CONTAINER, e, "init command line fail");
                    throw new FatalBeanException("init command line fail", e);
                }
                isInit = true;
            }
            INodeExtension newEx = addExtension((INodeExtension) bean);
            try
            {
                if (!unimportedSet.contains(newEx.getName()))
                {
                    final long startTime = System.currentTimeMillis();
                    newEx.init(ctx);
                    final long costTime = System.currentTimeMillis() - startTime;
//                    LOG.info(BSModule.CONTAINER, "extension init success, extension = %s, costTime = %s", newEx.getName(), DateUtil.getBeforeTimeStr(new Date(costTime)));
                }
            } catch (Throwable e)
            {
                if (newEx.isRequired())
                {
//                    LOG.error(BSModule.CONTAINER, e, "extension %s init fail", newEx);
                    throw new FatalBeanException(String.format("init extension %s fail", newEx), e);
                } else
                {
                    unimportedSet.add(newEx.getName());
//                    LOG.error(BSModule.CONTAINER, e, "extension %s init fail and stop to import it", newEx);
                }
            }
        }
        return bean;
    }

    @Override
    public void onApplicationEvent(SpringApplicationEvent event)
    {
        if (event instanceof ApplicationEnvironmentPreparedEvent)
        {
            //LOG.info(BSModule.CONTAINER, "\n%s", Banner.INIT);
            if (state == 0)
            {
                state = 1;
            } else
            {
//                LOG.warning(BSModule.CONTAINER, "environment prepared twice");
            }
        } else if (event instanceof ApplicationPreparedEvent)
        {
            if (state == 1)
            {
                initContext((ApplicationPreparedEvent) event);
//                LOG.info(BSModule.CONTAINER, "\n%s", Banner.INIT);
                state = 2;
            } else
            {
//                LOG.warning(BSModule.CONTAINER, "application int twice");
            }
        } else if (event instanceof ApplicationStartedEvent)
        {
            if (state == 2)
            {
                onStart((ApplicationStartedEvent) event);
//                LOG.info(BSModule.CONTAINER, "\n%s", Banner.START);
                state = 3;
            } else
            {
//                LOG.warning(BSModule.CONTAINER, "application start twice");
            }
        } else if (event instanceof ApplicationReadyEvent)
        {
            if (state == 3)
            {
                onReady((ApplicationReadyEvent) event);
//                LOG.warning(BSModule.CONTAINER, "\n%s", Banner.READY);
//                LOG.info(BSModule.CONTAINER, "this jar arg list: %s", allOps);
                for (INodeExtension e : extensions)
                {
                    if (!unimportedSet.contains(e.getName()))
                    {
//                        LOG.info(BSModule.CONTAINER, "successfully load extension %s", e.getClass().getName());
                    }
                }
                state = 4;
            } else
            {
//                LOG.warning(BSModule.CONTAINER, "application ready twice");
            }
        }
    }

    void addExcludeExtension(String eName)
    {
        unimportedSet.add(eName);
//        LOG.info(BSModule.CONTAINER, "extension %s is adding to exclusive list", eName);
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


    public void close(INodeContext ctx)
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
//                    LOG.error(BSModule.CONTAINER, e, "extension %s start fail", extension);
                    throw new ExtensionImportException(String.format("start extension %s fail", extension), e);
                } else
                {
                    addExcludeExtension(extension.getName());
//                    LOG.error(BSModule.CONTAINER, e, "extension %s start fail and stop to import it", extension);
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
//            LOG.info(BSModule.CONTAINER, "extension start success, extension = %s, costTime = %s", extensionCostTime.extension, extensionCostTime.costTime);
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
//                    LOG.error(BSModule.CONTAINER, e, "extension %s start fail", extension);
                    List<INodeExtension> toCloseList = new ArrayList<>();
                    for (int i = 0; i < index; ++i)
                    {
                        toCloseList.add(this.extensions.get(i));
                    }
                    close(ctx, toCloseList);
                    throw new ExtensionImportException(String.format("start extension %s fail", extension), e);
                } else
                {
//                    LOG.error(BSModule.CONTAINER, e, "extension %s start fail and stop to import it", extension);
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
//                LOG.error(BSModule.CONTAINER, e, "extension %s close fail", extension);
            }
        }
    }

    class KillThread extends Thread
    {

        @Override
        public void run()
        {
//            LOG.info(BSModule.CONTAINER, "node shutdown by signal");
            close(ctx);
        }
    }

//	private void onInit(ApplicationPreparedEvent event) {
//	init(ctx);
//}

//	void init(INodeContext ctx) {
//		for(INodeExtension extension : extensions) {
//			try {
//				if(!unimportedSet.contains(extension)) {
//					extension.init(ctx);
//				}
//			} catch(Throwable e) {
//				if(extension.isRequired()) {
//					LOG.error(BSModule.CONTAINER, e, "extension %s init fail", extension);
//					throw new ExtensionImportException(String.format("init extension %s fail", extension), e);
//				} else {
//					unimportedSet.add(extension);
//					LOG.warning(BSModule.CONTAINER, e, "extension %s init fail and stop to import it", extension);
//				}
//			}
//		}
//	}


    //private void onInstall(ApplicationEnvironmentPreparedEvent event) {
//		install(ctx);
    //}


    //
//		void install(INodeContext ctx) {
//			for(INodeExtension extension : extensions) {
//				try {
//					if(!unimportedSet.contains(extension)) {
//						extension.install(ctx);
//					}
//				} catch(Throwable e) {
//					if(extension.isRequired()) {
//						LOG.error(BSModule.CONTAINER, e, "extension %s install fail", extension);
//						throw new ExtensionImportException(String.format("install extension %s fail", extension), e);
//					} else {
//						unimportedSet.add(extension);
//						LOG.warning(BSModule.CONTAINER, e, "extension %s install fail and stop to import it", extension);
//					}
//				}
//			}
//		}

}
