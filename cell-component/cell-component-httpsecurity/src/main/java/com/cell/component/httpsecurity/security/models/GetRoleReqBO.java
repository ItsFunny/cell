package com.cell.component.httpsecurity.security.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Collection;

@Data
@ApiModel
public class GetRoleReqBO extends  AuthPermissionBaseReq
{
    @ApiModelProperty(value = "获取特定角色列表,可以为空")
    private Collection<Integer> roleIdList;
}
