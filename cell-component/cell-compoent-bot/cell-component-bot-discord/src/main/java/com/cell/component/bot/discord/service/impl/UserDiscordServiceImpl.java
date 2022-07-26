package com.cell.component.bot.discord.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cell.component.bot.discord.dao.UserDiscordDao;
import com.cell.component.bot.discord.entity.UserDiscord;
import com.cell.component.bot.discord.service.UserDiscordService;
import org.springframework.stereotype.Service;


@Service("userDiscordService")
public class UserDiscordServiceImpl extends ServiceImpl<UserDiscordDao, UserDiscord> implements UserDiscordService
{

}

