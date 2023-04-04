package com.cell.component.httpsecurity.security.models;

import com.mi.wallet.mange.context.IFromV4;
import com.mi.wallet.mange.context.PageRespDTO;
import com.mi.wallet.mange.entity.SecurityUserInfo;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;

@Data
@ApiModel
public class AuthAdminListRespBO extends PageRespDTO<Object, AuthAdminListRespBO.AdminListNode> {
    @Override
    protected AdminListNode conv(Object o) {
        return null;
    }

    @Data
    @ApiModel
    public static class AdminListNode implements IFromV4<SecurityUserInfo> {
        private Integer userId;
        private String name;
        @ApiModelProperty(value = "0未超级管理员,1为普通管理员")
        private Integer adminType;
        private Integer status;
        private Collection<RoleNode> items;

        @Override
        public void onFrom(SecurityUserInfo securityUserInfo) {
            this.userId = securityUserInfo.getUserId();
            this.name = securityUserInfo.getUserName();
            this.adminType = securityUserInfo.getType();
            this.status = securityUserInfo.getStatus();
        }
    }
}
