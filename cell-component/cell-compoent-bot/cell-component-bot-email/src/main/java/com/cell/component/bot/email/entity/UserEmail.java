package com.cell.component.bot.email.entity;

import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@SuppressWarnings("serial")
public class UserEmail extends Model<UserEmail>
{
    @TableId(type = IdType.AUTO)

    private Integer id;

    private String userKey;

    private String emailInfo;

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
