package com.cell.component.bot.common.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cell.component.bot.common.dao.GamePadsUserDao;
import com.cell.component.bot.common.entity.GamePadsUser;
import com.cell.component.bot.common.service.GamePadsUserService;
import org.springframework.stereotype.Service;


@Service("gamePadsUserService")
public class GamePadsUserServiceImpl extends ServiceImpl<GamePadsUserDao, GamePadsUser> implements GamePadsUserService
{

}

