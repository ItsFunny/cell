package com.cell.node.core.utils;

import com.cell.annotations.CellOrder;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.core.Conventions;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotatedTypeMetadata;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.Nullable;

import java.util.Map;

public class ExtensionClassUtil
{

    public static final String ORDER_ATTRIBUTE =
            Conventions.getQualifiedAttributeName(ConfigurationClassPostProcessor.class, "order");

    @Nullable
    public static Integer getOrder(AnnotationMetadata metadata)
    {
        Map<String, Object> orderAttributes = metadata.getAnnotationAttributes(CellOrder.class.getName());
        return (orderAttributes != null ? ((Integer) orderAttributes.get("value")) : null);
    }

    public static int getOrder(BeanDefinition beanDef)
    {
        Integer order = (Integer) beanDef.getAttribute(ORDER_ATTRIBUTE);
        return (order != null ? order : Ordered.LOWEST_PRECEDENCE);
    }

    @Nullable
    public static AnnotationAttributes attributesFor(AnnotatedTypeMetadata metadata, Class<?> annotationClass)
    {
        return attributesFor(metadata, annotationClass.getName());
    }

    @Nullable
    public static AnnotationAttributes attributesFor(AnnotatedTypeMetadata metadata, String annotationClassName)
    {
        return AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(annotationClassName, false));
    }
}
