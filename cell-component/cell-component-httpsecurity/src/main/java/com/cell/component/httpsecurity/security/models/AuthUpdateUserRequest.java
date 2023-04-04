package com.cell.component.httpsecurity.security.models;

import com.mi.wallet.mange.context.CellContext;
import com.mi.wallet.mange.context.IValidBasic;
import com.mi.wallet.mange.security.utils.SecurityUtils;
import com.mi.wallet.mange.utils.ValidUtils;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
public class AuthUpdateUserRequest implements IValidBasic {
    private Integer userId;
    @ApiModelProperty(name = "用户姓名")
    private String userName;
    private String password;
    // 0: super admin
    @ApiModelProperty(name = "是否是超级管理员", notes = "为0代表的是超级管理员,其他的则不是")
    private int adminType = 1;
    @ApiModelProperty(value = "角色列表")
    private List<Integer> roleIdList;
    @ApiModelProperty(value = "备注")
    private String mask;

    @Override
    public void validBasic(CellContext context) throws IllegalArgumentException {
        ValidUtils.check(this.userId);
        SecurityUtils.checkAdminType(this.getAdminType());
    }
}
