package com.cell.component.bot.common;

import com.cell.base.core.timewheel.ITimeWheelTaskExecutor;
import com.cell.component.bot.common.bo.MemberExistReqBO;
import com.cell.component.cache.service.ICacheService;
import com.cell.node.core.context.CellContext;
import com.cell.component.bot.common.bo.MemberExistRespBO;

public interface IBotService
{
    void start();

    void setTaskExecutor(ITimeWheelTaskExecutor ex);
    void setCacheService(ICacheService<String,String>cacheService);

    int getType();

    MemberExistRespBO exist(CellContext cellContext, MemberExistReqBO req);
}
