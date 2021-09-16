package com.cell.postprocessor;

import com.cell.adapter.AbstractBeanDefiinitionRegistry;
import com.cell.annotations.AutoPlugin;
import com.cell.annotations.ManagerNode;
import com.cell.config.ConfigConstants;
import com.cell.constants.BitConstants;
import com.cell.constants.OrderConstants;
import com.cell.context.InitCTX;
import com.cell.log.LOG;
import com.cell.manager.IReflectManager;
import com.cell.models.Module;
import com.cell.utils.ClassUtil;
import com.cell.utils.CollectionUtils;
import com.cell.utils.ReflectionUtils;
import com.cell.wrapper.AnnotaionManagerWrapper;
import com.cell.wrapper.AnnotationNodeWrapper;
import com.cell.wrapper.MonoWrapper;
import lombok.Data;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.core.PriorityOrdered;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-16 21:22
 */
@Data
public class ManagerFactoryPostProcessor extends AbstractBeanDefiinitionRegistry implements
        PriorityOrdered
{
    private Set<Class<?>> nodesSet;
    private Map<String, AnnotaionManagerWrapper> managers;

    // FIXME ,这个不应该丢在这里的,后续需要丢到scanStrategy中
    private Set<Class<?>> asBeanClz = new HashSet<>();

    private static final ManagerFactoryPostProcessor instance = new ManagerFactoryPostProcessor();

    public static ManagerFactoryPostProcessor getInstance()
    {
        return instance;
    }

    @Override
    protected void onPostProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
    {
        if (CollectionUtils.isEmpty(this.asBeanClz)) return;
        for (Class<?> aClass : asBeanClz)
        {
            this.defaultRegisterBean(registry, aClass);
        }
    }

    @Override
    protected void onPostProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException
    {
        factory.addBeanPostProcessor(new NodeBeanReplaceProcessor());
    }


    @Override
    protected void onInit(InitCTX ctx)
    {
        final Map<String, AnnotaionManagerWrapper> managers = (Map<String, AnnotaionManagerWrapper>) ctx.getData().get(ConfigConstants.MANAGERS);
        final Map<String, List<AnnotationNodeWrapper>> annotationNodesWp = (Map<String, List<AnnotationNodeWrapper>>) ctx.getData().get(ConfigConstants.annotationNodes);
        Set<String> keys = managers.keySet();


        Map<String, List<AnnotationNodeWrapper>> annotationNodes = new HashMap<>(annotationNodesWp);
        for (String key : keys)
        {
            if (annotationNodes.containsKey(key)) continue;
            annotationNodes.put(key, new ArrayList<>());
        }


        for (String key : annotationNodes.keySet())
        {
            List<AnnotationNodeWrapper> objects = annotationNodes.get(key);
            if (CollectionUtils.isEmpty(objects)) continue;
            Collections.sort(objects, (o1, o2) -> ClassUtil.ordererCompare(o1.getClass(), o2.getClass()));
            AnnotaionManagerWrapper annotaionManagerWrapper = managers.get(key);
            if (annotaionManagerWrapper == null)
            {
                continue;
            }
            String name = null;
            String group = null;
            boolean override = false;
            for (AnnotationNodeWrapper wp : objects)
            {
                Object object = wp.getNode();
                this.appendIfAsBeanNode(object.getClass());
                name = object.getClass().getAnnotation(ManagerNode.class).name();
                group = object.getClass().getAnnotation(ManagerNode.class).group();
                override = object.getClass().getAnnotation(ManagerNode.class).override();
                boolean contains = annotaionManagerWrapper.getManagerNodes().containsKey(name);
                if (contains)
                {
                    LOG.warn(Module.CONTAINER, "重复的node,group:{},name:{}", group, name);
                }
                if (!contains || override)
                {
                    annotaionManagerWrapper.getManagerNodes().put(name, wp);
                }
            }
        }
        this.managers = managers;

        this.fillNodeSet();
    }

    private void fillNodeSet()
    {
        this.nodesSet = new HashSet<>();
        Collection<AnnotaionManagerWrapper> values = this.managers.values();
        for (AnnotaionManagerWrapper value : values)
        {
            Map<String, AnnotationNodeWrapper> managerNodes = value.getManagerNodes();
            Collection<AnnotationNodeWrapper> nodes = managerNodes.values();
            for (AnnotationNodeWrapper node : nodes)
            {
                this.nodesSet.add(node.getNode().getClass());
            }
        }
    }

    @Override
    public int getOrder()
    {
        return 0;
    }

    public void finalHandleManager()
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

    private void appendIfAsBeanNode(Class<?> clz)
    {
        Mono<MonoWrapper<Boolean>> wp = ReflectionUtils.containAnnotaitonsInFieldOrMethod(clz, BitConstants.or, AutoPlugin.class, Autowired.class);
        wp.subscribe((v) ->
        {
            if (v.getRet()) this.asBeanClz.add(clz);
        });
    }

    class NodeBeanReplaceProcessor implements BeanPostProcessor
    {
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException
        {
            ManagerFactoryPostProcessor.this.replace(bean, beanName);
            return bean;
        }
    }

}
