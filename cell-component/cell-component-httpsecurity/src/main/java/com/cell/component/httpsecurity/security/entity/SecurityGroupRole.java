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
public class SecurityGroupRole extends Model<SecurityGroupRole> {
    @TableId(type = IdType.AUTO)

    private Integer id;

    private Integer groupId;

    private Integer roleId;

    private Date createDate;


    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.id;
    }
}
