package com.cell.bridge;

import com.cell.annotation.Plugin;
import com.cell.postprocessors.extension.PluginMethod;
import com.cell.utils.ExtensionClassUtil;
import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.annotation.RequiredAnnotationBeanPostProcessor;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.SourceExtractor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.MethodMetadata;
import org.springframework.util.Assert;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/*
    注册extension中被 @Plugin 所包裹的bean
 */
public class ExtensionClassBeanDefinitionReader
{

    private final BeanDefinitionRegistry registry;

    private final SourceExtractor sourceExtractor;

    /**
     * Create a new {@link ExtensionClassBeanDefinitionReader} instance that will be used
     * to populate the given {@link BeanDefinitionRegistry}.
     */
    public ExtensionClassBeanDefinitionReader(BeanDefinitionRegistry registry, SourceExtractor sourceExtractor)
    {

        this.registry = registry;
        this.sourceExtractor = sourceExtractor;
    }


    /**
     * Read {@code configurationModel}, registering bean definitions
     * with the registry based on its contents.
     */
    public void loadBeanDefinitions(Set<ExtensionClass> configurationModel)
    {
        for (ExtensionClass configClass : configurationModel)
        {
            loadBeanDefinitionsForConfigurationClass(configClass);
        }
    }

    private void loadBeanDefinitionsForConfigurationClass(
            ExtensionClass configClass)
    {

        for (PluginMethod beanMethod : configClass.getBeanMethods())
        {
            loadBeanDefinitionsForBeanMethod(beanMethod);
        }

    }

    private void loadBeanDefinitionsForBeanMethod(PluginMethod beanMethod)
    {
        ExtensionClass configClass = beanMethod.getExtensionClass();
        MethodMetadata metadata = beanMethod.getMetadata();
        String methodName = metadata.getMethodName();
        if (methodName.equals("getController"))
        {
            System.out.println(beanMethod);
        }

        if (configClass.skippedBeanMethods.contains(methodName))
        {
            return;
        }
        AnnotationAttributes bean = ExtensionClassUtil.attributesFor(metadata, Plugin.class);
        Assert.state(bean != null, "No @Plugin annotation attributes");

        // Consider name and any aliases
        List<String> names = new ArrayList<>(Arrays.asList(bean.getStringArray("name")));
        String beanName = (!names.isEmpty() ? names.remove(0) : methodName);
        if (beanName.equals(methodName))
        {
            beanName = metadata.getDeclaringClassName() + "_" + methodName;
        }

        // Register aliases even when overridden
        for (String alias : names)
        {
            this.registry.registerAlias(beanName, alias);
        }

        ExtensionClassBeanDefinition beanDef = new ExtensionClassBeanDefinition(configClass, metadata);
        beanDef.setResource(configClass.getResource());
        beanDef.setSource(this.sourceExtractor.extractSource(metadata, configClass.getResource()));

        if (metadata.isStatic())
        {
            // static @Plugin method
            beanDef.setBeanClassName(configClass.getMetadata().getClassName());
            beanDef.setFactoryMethodName(methodName);
        } else
        {
            // instance @Plugin method
            beanDef.setFactoryBeanName(configClass.getBeanName());
            beanDef.setUniqueFactoryMethodName(methodName);
        }
        beanDef.setAutowireMode(RootBeanDefinition.AUTOWIRE_CONSTRUCTOR);
        beanDef.setAttribute(RequiredAnnotationBeanPostProcessor.SKIP_REQUIRED_CHECK_ATTRIBUTE, Boolean.TRUE);


        // Replace the original bean definition with the target one, if necessary
        BeanDefinition beanDefToRegister = beanDef;
        // 注册bean
        this.registry.registerBeanDefinition(beanName, beanDefToRegister);
    }


    /**
     * {@link RootBeanDefinition} marker subclass used to signify that a bean definition
     * was created from a configuration class as opposed to any other configuration source.
     * Used in bean overriding cases where it's necessary to determine whether the bean
     * definition was created externally.
     */
    @SuppressWarnings("serial")
    private static class ExtensionClassBeanDefinition extends RootBeanDefinition implements AnnotatedBeanDefinition
    {

        private final AnnotationMetadata annotationMetadata;

        private final MethodMetadata factoryMethodMetadata;

        public ExtensionClassBeanDefinition(ExtensionClass configClass, MethodMetadata beanMethodMetadata)
        {
            this.annotationMetadata = configClass.getMetadata();
            this.factoryMethodMetadata = beanMethodMetadata;
            setLenientConstructorResolution(false);

        }

        private ExtensionClassBeanDefinition(ExtensionClassBeanDefinition original)
        {
            super(original);
            this.annotationMetadata = original.annotationMetadata;
            this.factoryMethodMetadata = original.factoryMethodMetadata;
        }

        @Override
        public AnnotationMetadata getMetadata()
        {
            return this.annotationMetadata;
        }

        @Override
        public MethodMetadata getFactoryMethodMetadata()
        {
            return this.factoryMethodMetadata;
        }

        @Override
        public boolean isFactoryMethod(Method candidate)
        {
            return (super.isFactoryMethod(candidate) && AnnotatedElementUtils.hasAnnotation(candidate, Plugin.class));
        }

        @Override
        public ExtensionClassBeanDefinition cloneBeanDefinition()
        {
            return new ExtensionClassBeanDefinition(this);
        }
    }


}
