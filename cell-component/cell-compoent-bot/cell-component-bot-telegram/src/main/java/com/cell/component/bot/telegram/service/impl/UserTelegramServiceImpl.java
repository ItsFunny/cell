package com.cell.component.bot.telegram.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cell.component.bot.telegram.entity.UserTelegram;
import com.cell.component.bot.telegram.service.UserTelegramService;
import com.cell.component.bot.telegram.dao.UserTelegramDao;
import org.springframework.stereotype.Service;


@Service("userTelegramService")
public class UserTelegramServiceImpl extends ServiceImpl<UserTelegramDao, UserTelegram> implements UserTelegramService
{

}

