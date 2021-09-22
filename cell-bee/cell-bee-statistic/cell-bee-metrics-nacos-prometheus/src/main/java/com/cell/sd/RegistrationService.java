//package com.cell.sd;
//
//import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
//import com.alibaba.nacos.common.notify.Event;
//import com.alibaba.nacos.common.notify.listener.Subscriber;
//import com.cell.annotations.ActivePlugin;
//import com.cell.config.AbstractInitOnce;
//import com.cell.context.InitCTX;
//import com.cell.discovery.NacosNodeDiscoveryImpl;
//import com.cell.log.LOG;
//import com.cell.model.ChangeItem;
//import com.cell.models.Module;
//import com.cell.service.INodeDiscovery;
//import org.springframework.cloud.client.ServiceInstance;
//import reactor.core.publisher.Mono;
//
//import java.util.*;
//import java.util.function.Supplier;
//
///**
// * @author Charlie
// * @When
// * @Description
// * @Detail
// * @Attention:
// * @Date 创建时间：2021-09-22 21:20
// */
//@ActivePlugin
//public class RegistrationService extends AbstractInitOnce
//{
//    private static final String[] NO_SERVICE_TAGS = new String[0];
//
//    private INodeDiscovery nodeDiscovery;
//    private volatile boolean onChange = false;
//
//
//    public Mono<ChangeItem<Map<String, String[]>>> getServiceNames(long waitMillis, Long index)
//    {
//        return returnDeferred(waitMillis, index, () ->
//        {
//            List<String> services = NacosNodeDiscoveryImpl.getInstance().getAllServices();
//            Set<String> set = new HashSet<String>();
//            set.addAll(services);
//
//            Map<String, String[]> result = new HashMap<String, String[]>();
//            for (String item : set)
//            {
//                result.put(item, NO_SERVICE_TAGS);
//            }
//            return result;
//        });
//    }
//
//    public Mono<ChangeItem<List<Map<String, Object>>>> getService(String appName, long waitMillis, Long index)
//    {
//        return returnDeferred(waitMillis, index, () ->
//        {
//            List<ServiceInstance> instances = this.nodeDiscovery.getInstances(appName);
//            List<Map<String, Object>> list = new ArrayList<>();
//
//            if (instances == null)
//            {
//                return Collections.emptyList();
//            } else
//            {
//                Set<ServiceInstance> instSet = new HashSet<>(instances);
//                for (ServiceInstance instance : instSet)
//                {
//                    Map<String, Object> ipObj = new HashMap<String, Object>();
//
//                    ipObj.put("Address", instance.getHost());
//                    ipObj.put("Node", instance.getServiceId());
//                    ipObj.put("ServiceAddress", instance.getHost());
//                    ipObj.put("ServiceName", instance.getServiceId());
//                    ipObj.put("ServiceID", instance.getHost() + ":" + instance.getPort());
//                    ipObj.put("ServicePort", instance.getPort());
//                    ipObj.put("NodeMeta", Collections.emptyMap());
//                    Map<String, String> metaJo = new HashMap<String, String>();
//                    metaJo.put("management.port", "" + instance.getPort());
//                    ipObj.put("ServiceMeta", metaJo);
//                    ipObj.put("ServiceTags", Collections.emptyList());
//
//                    list.add(ipObj);
//                }
//                return list;
//            }
//        });
//    }
//
//    private <T> Mono<ChangeItem<T>> returnDeferred(long waitMillis, Long index, Supplier<T> fn)
//    {
//        return Mono.just(new ChangeItem<>(fn.get(), new Date().getTime()));
//    }
//
//    @Override
//    protected void onInit(InitCTX ctx)
//    {
//        this.nodeDiscovery = NacosNodeDiscoveryImpl.getInstance();
//        ((NacosNodeDiscoveryImpl) this.nodeDiscovery).registerListen(new Listener());
//    }
//    class Listener extends Subscriber<InstancesChangeEvent>{
//        @Override
//        public void onEvent(InstancesChangeEvent event)
//        {
//            LOG.info(Module.SD_PROMETHEUS, "收到event:{},hosts:{}", event);
//            // FIXME , 处理nacos 的cluster
//            String clusters = event.getClusters();
//
//            List<com.alibaba.nacos.api.naming.pojo.Instance> hosts = event.getHosts();
//            synchronized (RegistrationService.this.delta)
//            {
//                RegistrationService.this.delta.put(event.getServiceName(), hosts);
//                RegistrationService.this.onChange = true;
//            }
//        }
//        @Override
//        public Class<? extends Event> subscribeType()
//        {
//            return InstancesChangeEvent.class;
//        }
//    }
//}
