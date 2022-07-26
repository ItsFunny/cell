package com.cell.node.core.context;

import com.cell.base.common.utils.StringUtils;
import lombok.Data;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;

@Data
public class ElapsedTimeInfos implements IElapsedTimeInfo
{
    private ReentrantLock lock;
    private Map<String, String> infoMap;
    private Set<String> schemaSet;

    private boolean sealed;
    private long elapsedTime;


    private List<String> mandatorySchemas;
    private List<String>optionalSchemas;

    @Override
    public void addInfo(String key, String info)
    {
        if (StringUtils.isEmpty(key) || StringUtils.isEmpty(info))
        {
            return;
        }
        if (!this.schemaSet.contains(key))
        {
            return;
        }
        try
        {
            this.lock.lock();
            this.infoMap.put(key, info);
        } finally
        {
            this.lock.unlock();
        }
    }

    public void dump()
    {
        try
        {
            StringBuilder mandatoryInfo = new StringBuilder();
            this.lock.lock();
            this.mandatorySchemas.stream().forEach(s ->
            {
                String info = this.infoMap.get(s);
                if (StringUtils.isNotEmpty(info))
                {
                    mandatoryInfo.append(String.format("%s<%s>", s, info));
                }
            });
            this.optionalSchemas.stream().forEach(s->{
                // TODO
            });
        } finally
        {
            this.lock.unlock();
        }
    }

    @Override
    public void setElapsedTime(long elapsedTime)
    {
        this.elapsedTime = elapsedTime;
    }

    @Override
    public long getElapsedTime()
    {
        return this.elapsedTime;
    }
}
