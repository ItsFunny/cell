package com.cell.manager;

import java.util.Collection;
import java.util.Set;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail 使用方法:
 * 编写一个类,实现nodeFactory即可, 然后添加对应的被管理的manager
 * 这样,当启动的时候,就会自动的管理manager旗下的所有node 组合在一起
 * 1. 编写一个类实现IManagerFactory
 * 2. 编写N个类,然后添加@ManagerNode即可 ,或者是通过实现IManagerNodeFactory实现
 * 参考: hook / filter
 * @Attention:
 * @Date 创建时间：2021-08-28 13:38
 */
public interface IReflectManager
{
    void invokeInterestNodes(Collection<Object> nodes);

    String name();

    boolean override();

}
