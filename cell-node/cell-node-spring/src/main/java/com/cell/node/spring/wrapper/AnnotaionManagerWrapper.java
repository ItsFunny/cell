package com.cell.node.spring.wrapper;

import com.cell.plugin.pipeline.manager.IReflectManager;
import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 09:33
 */
@Data
public class AnnotaionManagerWrapper
{
    IReflectManager manager;
    Map<String, AnnotationNodeWrapper> managerNodes = new ConcurrentHashMap<>();
}
