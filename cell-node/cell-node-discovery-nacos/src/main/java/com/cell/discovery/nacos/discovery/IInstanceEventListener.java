package com.cell.discovery.nacos.discovery;

import com.cell.base.core.events.IEvent;
import com.cell.model.Instance;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-08 12:28
 */
public interface IInstanceEventListener
{
    void onListen(InstanceEventWrapper wrapper);

    @Data
    class InstanceEventWrapper implements IEvent
    {
        public InstanceEventWrapper(Map<String, List<Instance>> instances)
        {
            this.instances = instances;
        }

        Map<String, List<com.cell.model.Instance>> instances;
    }
}
