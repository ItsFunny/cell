package com.cell.base.core.log.internal;

import com.cell.base.common.models.ModuleInterface;
import com.cell.base.common.utils.CollectionUtils;
import com.cell.base.core.log.ILogConsumer;
import com.cell.base.core.log.LogLevel;
import lombok.Data;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-07-10 15:30
 */
@Data
public class LogCache
{

    private TreeMap<Integer, LogPolicy> policiesMap = null;
    private List<LogPolicy> policies;
    private ThreadLocal<LogThreadCache> threadLocal;
    private AtomicInteger version;
    private AtomicBoolean started;
    private HashMap<Class<? extends ILogConsumer>, Long> logReceiverIdentifierMap;

    private Map<Class<? extends ILogConsumer>, ILogConsumer> consumers = new HashMap<>();

    public LogCache()
    {
        version = new AtomicInteger(0);
        this.started = new AtomicBoolean();
        threadLocal = new ThreadLocal<LogThreadCache>()
        {
            @Override
            protected LogThreadCache initialValue()
            {
                return new LogThreadCache();
            }
        };

        logReceiverIdentifierMap = new HashMap<>();

        policiesMap = new TreeMap<Integer, LogPolicy>(new Comparator<Integer>()
        {
            @Override
            public int compare(Integer o1, Integer o2)
            {
                return o1 - o2;
            }
        });
    }

    long makeKey(ModuleInterface module, LogLevel logLevel, Long logType)
    {
        long moduleId = module == null ? 0 : module.getModuleId();
        return (moduleId + (((long) logLevel.getValue()) << 16)
                + ((logType) << 32));
    }

    public synchronized void registerConsumer(Class<? extends ILogConsumer> key, ILogConsumer consumer)
    {
        this.consumers.put(key, consumer);
    }

    public void setPolicies(List<LogPolicy> policies)
    {
        synchronized (this)
        {
            Map<Class<? extends ILogConsumer>, Long> newLogReceiverIdentifierMap = new HashMap<>();
            TreeMap<Integer, LogPolicy> newPolicyMap = new TreeMap<>();

            Set<Class<? extends ILogConsumer>> receivers = new HashSet<>();
            for (LogPolicy policy : policies)
            {
                newPolicyMap.put(policy.getPriority(), policy);

                receivers.addAll(policy.getRecevierPolicies().keySet());
            }

            if (receivers.size() > 64)
            {
                throw new IllegalArgumentException("Max receivers is 64");
            }

            long identifier = 1L;
            for (Class<? extends ILogConsumer> receiverClass : receivers)
            {

                if (this.consumers.get(receiverClass) == null)
                {
                    continue;
                }

                newLogReceiverIdentifierMap.put(receiverClass, identifier);

                identifier *= 2;
            }

            logReceiverIdentifierMap.clear();
            logReceiverIdentifierMap.putAll(newLogReceiverIdentifierMap);

            policiesMap.clear();
            policiesMap.putAll(newPolicyMap);
            this.policies = policies;
        }
    }

    public Set<ILogConsumer> getLogReceivers(ModuleInterface module, LogLevel logLevel, Long logType)
    {
        LogThreadCache threadCache = threadLocal.get();
        Map<Long, Set<ILogConsumer>> logReceiverSetCacheByMask = threadCache.getLogReceiverSetCacheByMask();
        Map<Long, Set<ILogConsumer>> logReceiverSetCacheByKey = threadCache.getLogReceiverSetCacheByKey();
        int currentVersion = version.get();
        if (currentVersion != threadCache.getVersion())
        {
            logReceiverSetCacheByMask.clear();
            logReceiverSetCacheByKey.clear();
            threadCache.setVersion(currentVersion);
        }
        Long key = makeKey(module, logLevel, logType);
        Set<ILogConsumer> cache = logReceiverSetCacheByKey.get(key);
        if (cache != null)
        {
            return cache;
        }

        Long receiverMask = -1L;
        synchronized (this)
        {
            for (LogPolicy logPolicy : policiesMap.values())
            {
                // match module id.
                if (!logPolicy.matchModule(module))
                {
                    continue;
                }

                if (!logPolicy.matchLogLevel(logLevel))
                {
                    continue;
                }

                if (!logPolicy.matchLogType(logType))
                {
                    continue;
                }

                for (Map.Entry<Class<? extends ILogConsumer>, Boolean> entry : logPolicy.getRecevierPolicies()
                        .entrySet())
                {

                    Long identifier = logReceiverIdentifierMap.get(entry.getKey());
                    if (identifier == null)
                    {
                        continue;
                    }
                    if (entry.getValue())
                    {
                        receiverMask |= identifier;
                    } else
                    {
                        receiverMask &= ~identifier;
                    }
                }
            }
        }
        Set<ILogConsumer> receivers = logReceiverSetCacheByMask.get(receiverMask);
        if (CollectionUtils.isEmpty(receivers))
        {

            receivers = new HashSet<>();
//            while (!this.started.get())
//            {
//                try
//                {
//                    TimeUnit.MICROSECONDS.sleep(200);
//                } catch (InterruptedException e)
//                {
//                    e.printStackTrace();
//                }
//            }
            synchronized (this)
            {
                for (Map.Entry<Class<? extends ILogConsumer>, Long> entry : logReceiverIdentifierMap.entrySet())
                {
                    if ((entry.getValue() & receiverMask) != 0L)
                    {
                        ILogConsumer receiver = this.consumers.get(entry.getKey());

                        if (receiver == null)
                        {
                        } else
                        {
                            receivers.add(receiver);
                        }
                    }
                }
            }
            logReceiverSetCacheByMask.put(receiverMask, receivers);
            if (key != null)
            {
                logReceiverSetCacheByKey.put(key, receivers);
            }
        }

        return receivers;
    }

    public void start()
    {
        this.started.compareAndSet(false, true);
    }
}
