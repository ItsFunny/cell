package com.cell.component.httpsecurity.security.impl;

import com.cell.base.common.utils.CollectionUtils;
import com.mi.wallet.mange.security.models.MenuNode;
import com.mi.wallet.mange.security.models.OperationNode;
import com.mi.wallet.mange.security.models.RoleNode;
import com.mi.wallet.mange.security.models.UserNode;
import lombok.Data;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2022/4/11 10:18 下午
 */
@Data
public class UserAuthority {
    // 用户拥有的角色
    private Set<String> roles = new HashSet<>(1);
    // 用户拥有的所有数据权限
    private Set<String> dataPermissionSet = new HashSet<>(1);
    // 用户拥有的所有操作权限
    private Set<String> operationPermissionSet = new HashSet<>(1);

    private boolean sealed;

    public void seal(UserNode userNode) {
        if (sealed) {
            return;
        }
        Collection<RoleNode> roleNodes = userNode.getItems();
        if (CollectionUtils.isEmpty(roleNodes)) {
            return;
        }
        Consumer<RoleNode> consumer = (roleNode) -> {
            this.roles.add(roleNode.getRoleName());
            List<MenuNode> menuNodes = roleNode.getMenuItems();
            for (MenuNode menuNode : menuNodes) {
                this.dataPermissionSet.add(menuNode.getMenuName());
                List<OperationNode> operationNodes = menuNode.getOperationItems();
                for (OperationNode operationNode : operationNodes) {
                    this.operationPermissionSet.add(operationNode.getOperationRule());
                }
            }
        };
        for (RoleNode roleNode : roleNodes) {
            consumer.accept(roleNode);
            if (CollectionUtils.isNotEmpty(roleNode.getChildRoleItems())) {
                List<RoleNode> childRoleItems = roleNode.getChildRoleItems();
                for (RoleNode childRoleItem : childRoleItems) {
                    consumer.accept(childRoleItem);
                }
            }
        }
        this.sealed = true;
    }
}
