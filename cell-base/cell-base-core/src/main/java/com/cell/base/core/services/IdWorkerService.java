/**
 * @Description
 * @author joker
 * @date 创建时间：2018年7月1日 下午6:55:30
 */
package com.cell.base.core.services;

import java.util.Collection;

public interface IdWorkerService
{

    long nextId();

    Collection<Long> nextIds(int num);

}