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
public class SecurityGroup extends Model<SecurityGroup> {
    @TableId(type = IdType.AUTO)
    /**组ID*/
    private Integer groupId;
    /**
     * 父组ID,为0的时候代表的是顶级组
     */
    private Integer parentGroupId;
    /**
     * 组名称
     */
    private String groupName;

    private Integer groupLevel;
    /**
     * 权重,小于100
     */
    private Integer groupWeight;
    /**
     * 组描述
     */
    private String groupDesc;
    /**
     * 用户总数
     */
    private Integer userAmount;

    private Date createDate;

    private Date updateDate;


    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.groupId;
    }
}
