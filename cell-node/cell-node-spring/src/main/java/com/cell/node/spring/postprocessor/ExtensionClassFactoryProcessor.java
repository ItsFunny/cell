package com.cell.node.spring.postprocessor;

import com.cell.base.common.models.Module;
import com.cell.base.common.context.AbstractInitOnce;
import com.cell.base.common.context.InitCTX;
import com.cell.sdk.log.LOG;
import com.cell.node.core.utils.ExtensionClassUtil;
import com.cell.node.spring.adapter.AbstractBeanDefiinitionRegistry;
import com.cell.node.spring.adapter.IBeanPostProcessortAdapter;
import com.cell.node.spring.bridge.ExtensionClass;
import com.cell.node.spring.bridge.ExtensionClassBeanDefinitionReader;
import com.cell.node.spring.bridge.ExtensionClassParser;
import com.cell.node.spring.constants.SpringBridge;
import com.cell.node.spring.postprocessors.extension.SpringExtensionManager;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.parsing.FailFastProblemReporter;
import org.springframework.beans.factory.parsing.PassThroughSourceExtractor;
import org.springframework.beans.factory.parsing.ProblemReporter;
import org.springframework.beans.factory.parsing.SourceExtractor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.classreading.CachingMetadataReaderFactory;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.util.*;

public class ExtensionClassFactoryProcessor extends AbstractBeanDefiinitionRegistry implements BeanDefinitionRegistryPostProcessor,
        PriorityOrdered, ResourceLoaderAware, BeanClassLoaderAware
{

    private static final ExtensionClassFactoryProcessor instance = new ExtensionClassFactoryProcessor();

    public static ExtensionClassFactoryProcessor getInstance()
    {
        return instance;
    }

    private SourceExtractor sourceExtractor = new PassThroughSourceExtractor();

    private ProblemReporter problemReporter = new FailFastProblemReporter();


    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    @Nullable
    private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();

    private MetadataReaderFactory metadataReaderFactory = new CachingMetadataReaderFactory();

    private boolean setMetadataReaderFactoryCalled = false;

    private final Set<Integer> registriesPostProcessed = new HashSet<>();

    private final Set<Integer> factoriesPostProcessed = new HashSet<>();

    @Nullable
    private ExtensionClassBeanDefinitionReader reader;

    public ExtensionClassFactoryProcessor()
    {
    }

    // 必须在 plugin 注册之后
    @Override
    public int getOrder()
    {
        return Ordered.LOWEST_PRECEDENCE;  // within PriorityOrdered
    }

    public void setSourceExtractor(@Nullable SourceExtractor sourceExtractor)
    {
        this.sourceExtractor = (sourceExtractor != null ? sourceExtractor : new PassThroughSourceExtractor());
    }

    public void setProblemReporter(@Nullable ProblemReporter problemReporter)
    {
        this.problemReporter = (problemReporter != null ? problemReporter : new FailFastProblemReporter());
    }

    public void setMetadataReaderFactory(MetadataReaderFactory metadataReaderFactory)
    {
        Assert.notNull(metadataReaderFactory, "MetadataReaderFactory must not be null");
        this.metadataReaderFactory = metadataReaderFactory;
        this.setMetadataReaderFactoryCalled = true;
    }


    @Override
    public void setResourceLoader(ResourceLoader resourceLoader)
    {
        Assert.notNull(resourceLoader, "ResourceLoader must not be null");
        this.resourceLoader = resourceLoader;
        if (!this.setMetadataReaderFactoryCalled)
        {
            this.metadataReaderFactory = new CachingMetadataReaderFactory(resourceLoader);
        }
    }

    @Override
    public void setBeanClassLoader(ClassLoader beanClassLoader)
    {
        this.beanClassLoader = beanClassLoader;
        if (!this.setMetadataReaderFactoryCalled)
        {
            this.metadataReaderFactory = new CachingMetadataReaderFactory(beanClassLoader);
        }
    }


    @Override
    protected void onPostProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
    {
        int registryId = System.identityHashCode(registry);
        if (this.registriesPostProcessed.contains(registryId))
        {
            throw new IllegalStateException(
                    "postProcessBeanDefinitionRegistry already called on this post-processor against " + registry);
        }
        if (this.factoriesPostProcessed.contains(registryId))
        {
            throw new IllegalStateException(
                    "postProcessBeanFactory already called on this post-processor against " + registry);
        }
        this.registriesPostProcessed.add(registryId);

        processConfigBeanDefinitions(registry);
    }

    @Override
    protected void onPostProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException
    {

    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
    {
        int factoryId = System.identityHashCode(beanFactory);
        if (this.factoriesPostProcessed.contains(factoryId))
        {
            throw new IllegalStateException(
                    "postProcessBeanFactory already called on this post-processor against " + beanFactory);
        }
        this.factoriesPostProcessed.add(factoryId);
        if (!this.registriesPostProcessed.contains(factoryId))
        {
            // BeanDefinitionRegistryPostProcessor hook apparently not supported...
            // Simply call processConfigurationClasses lazily at this point then.
            processConfigBeanDefinitions((BeanDefinitionRegistry) beanFactory);
        }
        //enhanceConfigurationClasses(beanFactory);
    }

    public void processConfigBeanDefinitions(BeanDefinitionRegistry registry)
    {
        List<BeanDefinitionHolder> configCandidates = new ArrayList<>();
        String[] candidateNames = registry.getBeanDefinitionNames();

        for (String beanName : candidateNames)
        {
            BeanDefinition beanDef = registry.getBeanDefinition(beanName);
            Object isExtension = beanDef.getAttribute(SpringBridge.EXTENSION_FLAG_ATTR);
            Object attribute = beanDef.getAttribute(SpringBridge.ACTIVE_CFG_ATTR);
            if (isExtension != null || attribute != null)
            {
                configCandidates.add(new BeanDefinitionHolder(beanDef, beanName));
                if (registry instanceof ConfigurableListableBeanFactory)
                {
                    ConfigurableListableBeanFactory factory = (ConfigurableListableBeanFactory) registry;
                    if (beanDef instanceof GenericBeanDefinition)
                    {
                        GenericBeanDefinition bd = (GenericBeanDefinition) (beanDef);
                        factory.getBean((String) beanDef.getAttribute(SpringBridge.BEAN_NAME_ATTR));
                    }
                }
            }
        }

        // Return immediately if no @Configuration classes were found
        if (configCandidates.isEmpty())
        {
            return;
        }

        // Sort by previously determined @Order value, if applicable
        configCandidates.sort((bd1, bd2) ->
        {
            int i1 = ExtensionClassUtil.getOrder(bd1.getBeanDefinition());
            int i2 = ExtensionClassUtil.getOrder(bd2.getBeanDefinition());
            return Integer.compare(i1, i2);
        });

        Set<BeanDefinitionHolder> candidates = new LinkedHashSet<>();
        for (BeanDefinitionHolder holder : configCandidates)
        {
            if (holder.getBeanDefinition() instanceof GenericBeanDefinition)
            {
                GenericBeanDefinition gdef = (GenericBeanDefinition) holder.getBeanDefinition();
                if (SpringExtensionManager.getInstance().isExclusive(gdef.getBeanClass().getSimpleName()))
                {
                    LOG.info(Module.CONTAINER, "exclusive extension: %s", gdef.getBeanClass());
                } else
                {
                    candidates.add(holder);
                }
            }
        }
        // Parse each Node Extension class
        ExtensionClassParser parser = new ExtensionClassParser(
                this.metadataReaderFactory, this.problemReporter, this.resourceLoader);

        //Set<BeanDefinitionHolder> candidates = new LinkedHashSet<>(configCandidates);
        parser.parse(candidates);
        parser.validate();
        // 开始实例化,重新注册为bean
        Set<ExtensionClass> configClasses = new LinkedHashSet<>(parser.getExtensionClasses());

        // Read the model and create bean definitions based on its content
        if (this.reader == null)
        {
            this.reader = new ExtensionClassBeanDefinitionReader(registry, this.sourceExtractor);
        }
        this.reader.loadBeanDefinitions(configClasses);

        if (this.metadataReaderFactory instanceof CachingMetadataReaderFactory)
        {
            // Clear cache in externally provided MetadataReaderFactory; this is a no-op
            // for a shared cache since it'll be cleared by the ApplicationContext.
            ((CachingMetadataReaderFactory) this.metadataReaderFactory).clearCache();
        }
    }


    @Override
    protected void onInit(InitCTX ctx)
    {
        new innerInit().initOnce(ctx);
    }

    @Override
    public List<Class<? extends IBeanPostProcessortAdapter>> getToRegistryPostProcessor()
    {
        return null;
    }

    private class innerInit extends AbstractInitOnce
    {
        @Override
        protected void onInit(InitCTX ctx)
        { }
    }
}
