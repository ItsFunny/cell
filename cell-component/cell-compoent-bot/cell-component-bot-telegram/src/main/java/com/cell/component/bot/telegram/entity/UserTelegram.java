package com.cell.component.bot.telegram.entity;

import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@SuppressWarnings("serial")
public class UserTelegram extends Model<UserTelegram>
{
    @TableId(type = IdType.AUTO)

    private Integer id;
    /**
     * 可以是用户名称,也可以是其他关键字
     */
    private String userKey;
    /**
     * 0 未绑定
     */
    private Integer status;

    private Date createDate;
    /**
     * telegram的userId
     */
    private String telegramUserId;


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
