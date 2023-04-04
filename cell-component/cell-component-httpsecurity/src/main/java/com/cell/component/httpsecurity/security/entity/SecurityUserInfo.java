package com.cell.component.httpsecurity.security.entity;

import java.util.Date;

import com.baomidou.mybatisplus.extension.activerecord.Model;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("serial")
public class SecurityUserInfo extends Model<SecurityUserInfo> {
    @TableId(type = IdType.AUTO)
    /**用户ID*/
    private Integer userId;

    private String userName;
    /**
     * 头像地址
     */
    private String haedImg;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 管理员类型,为1代表的是普通管理员,0 代表超管
     */
    private Integer type;
    /**
     * 0 为不可用, 1 enable 但是未kyc, 2 kyc认证
     */
    private Integer status;
    /**
     * 备注
     */
    private String mask;

    private Date createDate;

    private Date updateDate;


    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.userId;
    }
}
