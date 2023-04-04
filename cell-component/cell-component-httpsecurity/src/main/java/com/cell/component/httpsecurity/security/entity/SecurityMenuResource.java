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
public class SecurityMenuResource extends Model<SecurityMenuResource> {
    @TableId(type = IdType.AUTO)
    /**菜单表ID*/
    private Integer menuResourceId;
    /**
     * permission表id,通过该id 关联
     */
    private Integer permissionId;
    /**
     * 菜单名称
     */
    private String menuName;
    /**
     * url
     */
    private String menuUrl;
    /**
     * 上级菜单id,为0代表顶级菜单
     */
    private Integer parentMenuId;

    private Date createDate;

    private Date updateDate;


    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.menuResourceId;
    }
}
