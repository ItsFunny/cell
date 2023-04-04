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
public class SecurityPermission extends Model<SecurityPermission> {
    @TableId(type = IdType.AUTO)

    private Integer permissionId;
    /**
     * 权限类型
     */
    private String permissionType;

    private String permissionDesc;

    private Date createDate;

    private Date updateDate;


    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.permissionId;
    }
}
