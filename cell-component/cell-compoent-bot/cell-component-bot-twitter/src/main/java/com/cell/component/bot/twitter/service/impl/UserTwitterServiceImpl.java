package com.cell.component.bot.twitter.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cell.component.bot.twitter.dao.UserTwitterDao;
import com.cell.component.bot.twitter.entity.UserTwitter;
import com.cell.component.bot.twitter.service.UserTwitterService;
import org.springframework.stereotype.Service;


@Service("userTwitterService")
public class UserTwitterServiceImpl extends ServiceImpl<UserTwitterDao, UserTwitter> implements UserTwitterService
{

}

