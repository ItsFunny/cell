package com.cell.component.bot.twitter.entity;

import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@SuppressWarnings("serial")
public class UserTwitter extends Model<UserTwitter>
{
    @TableId(type = IdType.AUTO)

    private Integer id;
    /**
     * 用户的唯一id
     */
    private String userKey;
    /**
     * twitter的id
     */
    private String twitterId;
    /**
     * 0 未绑定
     */
    private Integer status;

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
