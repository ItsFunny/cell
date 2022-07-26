package com.cell.component.bot.proxy.service;

import com.cell.component.bot.common.IBotService;

import java.util.List;

public interface IProxyBot extends  IBotService
{
    void setBots(List<IBotService> bots);
}
