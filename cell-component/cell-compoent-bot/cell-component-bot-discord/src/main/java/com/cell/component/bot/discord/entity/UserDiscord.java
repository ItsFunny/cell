package com.cell.component.bot.discord.entity;

import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@SuppressWarnings("serial")
public class UserDiscord extends Model<UserDiscord>
{
    @TableId(type = IdType.AUTO)

    private Integer id;

    private String userKey;

    private Integer status;

    private String discordUserId;

    private Date createDate;


    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    protected Serializable pkVal()
    {
        return this.id;
    }
}
