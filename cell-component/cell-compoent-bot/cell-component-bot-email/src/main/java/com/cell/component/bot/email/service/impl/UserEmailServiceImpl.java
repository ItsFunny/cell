package com.cell.component.bot.email.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cell.component.bot.email.service.UserEmailService;
import com.cell.component.bot.email.dao.UserEmailDao;
import com.cell.component.bot.email.entity.UserEmail;
import org.springframework.stereotype.Service;


@Service("userEmailService")
public class UserEmailServiceImpl extends ServiceImpl<UserEmailDao, UserEmail> implements UserEmailService
{

}

