package com.cell.node.spring.bridge;

import com.cell.annotations.Plugin;
import com.cell.log.LOG;
import com.cell.models.Module;
import com.cell.node.core.utils.ExtensionClassUtil;
import com.cell.node.spring.postprocessors.extension.PluginMethod;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.parsing.ProblemReporter;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.core.NestedIOException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.core.type.classreading.MetadataReader;
import org.springframework.core.type.classreading.MetadataReaderFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.*;

public class ExtensionClassParser
{
    private final MetadataReaderFactory metadataReaderFactory;
    private final ProblemReporter problemReporter;
    private final ResourceLoader resourceLoader;
    private final Map<ExtensionClass, ExtensionClass> extensionClasses = new LinkedHashMap<>();


    public ExtensionClassParser(MetadataReaderFactory metadataReaderFactory,
                                ProblemReporter problemReporter, ResourceLoader resourceLoader)
    {

        this.metadataReaderFactory = metadataReaderFactory;
        this.problemReporter = problemReporter;
        this.resourceLoader = resourceLoader;
    }

    // 解析extension内部的函数
    public void parse(Set<BeanDefinitionHolder> configCandidates)
    {
        for (BeanDefinitionHolder holder : configCandidates)
        {
            BeanDefinition bd = holder.getBeanDefinition();
            try
            {
                if (bd instanceof AnnotatedBeanDefinition)
                {
                    parse(((AnnotatedBeanDefinition) bd).getMetadata(), holder.getBeanName());
                } else if (bd instanceof AbstractBeanDefinition && ((AbstractBeanDefinition) bd).hasBeanClass())
                {
                    parse(((AbstractBeanDefinition) bd).getBeanClass(), holder.getBeanName());
                } else
                {
                    parse(bd.getBeanClassName(), holder.getBeanName());
                }
            } catch (BeanDefinitionStoreException ex)
            {
                throw ex;
            } catch (Throwable ex)
            {
                throw new BeanDefinitionStoreException(
                        "Failed to parse configuration class [" + bd.getBeanClassName() + "]", ex);
            }
        }
    }

    protected final void parse(@Nullable String className, String beanName) throws IOException
    {
        Assert.notNull(className, "No bean class name for configuration class bean definition");
        MetadataReader reader = this.metadataReaderFactory.getMetadataReader(className);
        processConfigurationClass(new ExtensionClass(reader, beanName));
    }

    protected final void parse(Class<?> clazz, String beanName) throws IOException
    {
        processConfigurationClass(new ExtensionClass(clazz, beanName));
    }

    protected final void parse(AnnotationMetadata metadata, String beanName) throws IOException
    {
        processConfigurationClass(new ExtensionClass(metadata, beanName));
    }

    public void validate()
    {
        for (ExtensionClass configClass : this.extensionClasses.keySet())
        {
            configClass.validate(this.problemReporter);
        }
    }

    public Set<ExtensionClass> getExtensionClasses()
    {
        return this.extensionClasses.keySet();
    }


    protected void processConfigurationClass(ExtensionClass configClass) throws IOException
    {

        // Recursively process the configuration class and its superclass hierarchy.
        SourceClass sourceClass = asSourceClass(configClass);
        do
        {
            sourceClass = doProcessConfigurationClass(configClass, sourceClass);
        }
        while (sourceClass != null);

        this.extensionClasses.put(configClass, configClass);
    }

    @Nullable
    protected final SourceClass doProcessConfigurationClass(ExtensionClass configClass, SourceClass sourceClass)
            throws IOException
    {

        // Process individual @Bean methods // 获取被 @Plugin 所标识的方法
        Set<MethodMetadata> beanMethods = retrieveBeanMethodMetadata(sourceClass);
        for (MethodMetadata methodMetadata : beanMethods)
        {
            configClass.addBeanMethod(new PluginMethod(methodMetadata, configClass));
        }


        // No superclass -> processing is complete
        return null;
    }


    /**
     * Retrieve the metadata for all <code>@Bean</code> methods.
     */
    private Set<MethodMetadata> retrieveBeanMethodMetadata(SourceClass sourceClass)
    {
        AnnotationMetadata original = sourceClass.getMetadata();
        Set<MethodMetadata> beanMethods = original.getAnnotatedMethods(Plugin.class.getName());
        if (beanMethods.size() > 1 && original instanceof StandardAnnotationMetadata)
        {
            // Try reading the class file via ASM for deterministic declaration order...
            // Unfortunately, the JVM's standard reflection returns methods in arbitrary
            // order, even between different runs of the same application on the same JVM.
            try
            {
                AnnotationMetadata asm =
                        this.metadataReaderFactory.getMetadataReader(original.getClassName()).getAnnotationMetadata();
                Set<MethodMetadata> asmMethods = asm.getAnnotatedMethods(Plugin.class.getName()); // 通过这里可以指定具体某些作为 @Bean的替代
                if (asmMethods.size() >= beanMethods.size())
                {
                    Set<MethodMetadata> selectedMethods = new LinkedHashSet<>(asmMethods.size());
                    for (MethodMetadata asmMethod : asmMethods)
                    {
                        for (MethodMetadata beanMethod : beanMethods)
                        {
                            if (beanMethod.getMethodName().equals(asmMethod.getMethodName()))
                            {
                                selectedMethods.add(beanMethod);
                                break;
                            }
                        }
                    }
                    if (selectedMethods.size() == beanMethods.size())
                    {
                        // All reflection-detected methods found in ASM method set -> proceed
                        beanMethods = selectedMethods;
                    }
                }
            } catch (IOException ex)
            {
                LOG.debug(Module.CONTAINER, "Failed to read class file via ASM for determining @Plugin method order", ex);
                // No worries, let's continue with the reflection metadata we started with...
            }
        }
        return beanMethods;
    }

    private SourceClass asSourceClass(ExtensionClass configurationClass) throws IOException
    {
        AnnotationMetadata metadata = configurationClass.getMetadata();
        if (metadata instanceof StandardAnnotationMetadata)
        {
            return asSourceClass(((StandardAnnotationMetadata) metadata).getIntrospectedClass());
        }
        return asSourceClass(metadata.getClassName());
    }

    /**
     * Factory method to obtain a {@link SourceClass} from a {@link Class}.
     */
    SourceClass asSourceClass(@Nullable Class<?> classType) throws IOException
    {
        if (classType == null)
        {
            return new SourceClass(Object.class);
        }
        try
        {
            // Sanity test that we can reflectively read annotations,
            // including Class attributes; if not -> fall back to ASM
            for (Annotation ann : classType.getAnnotations())
            {
                AnnotationUtils.validateAnnotation(ann);
            }
            return new SourceClass(classType);
        } catch (Throwable ex)
        {
            // Enforce ASM via class name resolution
            return asSourceClass(classType.getName());
        }
    }

    /**
     * Factory method to obtain {@link SourceClass SourceClasss} from class names.
     */
    private Collection<SourceClass> asSourceClasses(String... classNames) throws IOException
    {
        List<SourceClass> annotatedClasses = new ArrayList<>(classNames.length);
        for (String className : classNames)
        {
            annotatedClasses.add(asSourceClass(className));
        }
        return annotatedClasses;
    }

    /**
     * Factory method to obtain a {@link SourceClass} from a class name.
     */
    SourceClass asSourceClass(@Nullable String className) throws IOException
    {
        if (className == null)
        {
            return new SourceClass(Object.class);
        }
        if (className.startsWith("java"))
        {
            // Never use ASM for core java types
            try
            {
                return new SourceClass(ClassUtils.forName(className, this.resourceLoader.getClassLoader()));
            } catch (ClassNotFoundException ex)
            {
                throw new NestedIOException("Failed to load class [" + className + "]", ex);
            }
        }
        return new SourceClass(this.metadataReaderFactory.getMetadataReader(className));
    }


    /**
     * Simple wrapper that allows annotated source classes to be dealt with
     * in a uniform manner, regardless of how they are loaded.
     */
    private class SourceClass implements Ordered
    {

        private final Object source;  // Class or MetadataReader

        private final AnnotationMetadata metadata;

        public SourceClass(Object source)
        {
            this.source = source;
            if (source instanceof Class)
            {
                this.metadata = new StandardAnnotationMetadata((Class<?>) source, true);
            } else
            {
                this.metadata = ((MetadataReader) source).getAnnotationMetadata();
            }
        }

        public final AnnotationMetadata getMetadata()
        {
            return this.metadata;
        }

        @Override
        public int getOrder()
        {
            Integer order = ExtensionClassUtil.getOrder(this.metadata);
            return (order != null ? order : Ordered.LOWEST_PRECEDENCE);
        }


        @Override
        public boolean equals(Object other)
        {
            return (this == other || (other instanceof SourceClass &&
                    this.metadata.getClassName().equals(((SourceClass) other).metadata.getClassName())));
        }

        @Override
        public int hashCode()
        {
            return this.metadata.getClassName().hashCode();
        }

        @Override
        public String toString()
        {
            return this.metadata.getClassName();
        }
    }


}
