package com.cell.log.internal;

import com.cell.base.common.models.ModuleInterface;
import com.cell.log.ILogConsumer;
import com.cell.log.LogLevel;
import lombok.Data;

import java.util.List;
import java.util.Map;


@Data
public class LogPolicy
{
    // 指定 module 和 logType和logLevel 可以指定policy
    private List<Integer> moduleIds;
    private List<Long> logTypes;
    private List<Integer> logLevels;
    private Map<String, List<String>> userFilters;
    private Map<Class<? extends ILogConsumer>, Boolean> recevierPolicies;

    private int priority;

    public boolean matchModule(ModuleInterface module)
    {
        return moduleIds == null || moduleIds.size() == 0 || (moduleIds.contains((int) module.getModuleId()));
    }

    public boolean matchLogType(Long logType)
    {
        return logTypes == null || logTypes.size() == 0 || (logTypes.contains(logType));
    }


    public boolean matchLogLevel(LogLevel logLevel)
    {
        return logLevels == null || logLevels.size() == 0 || (logLevels.contains((int) logLevel.getValue()));
    }


    public boolean matchUserFilters(Map<String, String> userFilters)
    {
        if (this.userFilters == null || this.userFilters.size() == 0)
        {
            return true;
        }

        if (userFilters == null || userFilters.size() == 0)
        {
            return false;
        }

        for (Map.Entry<String, List<String>> entry : this.userFilters.entrySet())
        {
            String exp = userFilters.get(entry.getKey());
            if (exp == null)
            {
                return false;
            }

            if (!entry.getValue().contains(exp))
            {
                return false;
            }
        }

        return true;
    }


    public LogPolicy(List<Integer> moduleIds, List<Long> logTypes, List<Integer> logLevels, Map<String, List<String>> userFilters,
                     Map<Class<? extends ILogConsumer>, Boolean> recevierPolicies, int priority)
    {
        super();
        this.moduleIds = moduleIds;
        this.logTypes = logTypes;
        this.logLevels = logLevels;
        this.userFilters = userFilters;
        this.recevierPolicies = recevierPolicies;
        this.priority = priority;
    }

    public LogPolicy()
    {
    }
}
