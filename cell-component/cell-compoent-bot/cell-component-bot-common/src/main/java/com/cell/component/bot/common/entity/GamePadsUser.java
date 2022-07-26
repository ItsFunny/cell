package com.cell.component.bot.common.entity;

import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@SuppressWarnings("serial")
public class GamePadsUser extends Model<GamePadsUser>
{
    @TableId(type = IdType.AUTO)

    private Integer id;

    private String address;

    private String nickName;
    /**
     * 上一次查询时间
     */
    private Date activeSince;

    private Date createTime;

    private Date updateNickTime;

    private String userAvatar;

    private String twitter;

    private String telegram;

    private String discord;

    private String email;


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
