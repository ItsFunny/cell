package com.cell.wrapper;

import com.cell.manager.IReflectManager;
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
    Map<String, Object> managerNodes = new ConcurrentHashMap<>();

}
