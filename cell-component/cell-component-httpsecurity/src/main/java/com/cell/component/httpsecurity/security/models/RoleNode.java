package com.cell.component.httpsecurity.security.models;


import com.cell.base.common.utils.CollectionUtils;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import net.minidev.json.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@ApiModel
public class RoleNode {
    private Integer roleId;
    private String roleName;
    private String roleDesc;
    private List<MenuNode> menuItems;
    @ApiModelProperty(value = "子角色")
    private List<RoleNode> childRoleItems;

    @JsonIgnore
    private Map<Integer, RoleNode> permissionRoleMap;

    public void addMenu(MenuNode node) {
        Integer permissionId = node.getPermissionId();
        this.menuItems.add(node);
    }

    public void addChildRole(RoleNode roleNode) {
        if (CollectionUtils.isEmpty(this.childRoleItems)) {
            this.childRoleItems = new ArrayList<>();
        }
        this.childRoleItems.add(roleNode);
    }

    public List<Integer> getRoleIds() {
        List<Integer> roleIds = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(this.childRoleItems)) {
            for (RoleNode roleNode : this.childRoleItems) {
                roleIds.add(roleNode.getRoleId());
            }
        }
        roleIds.add(this.roleId);
        return roleIds;
    }
}
