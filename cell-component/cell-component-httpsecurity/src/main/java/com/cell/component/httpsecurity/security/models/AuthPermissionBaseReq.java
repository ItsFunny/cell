package com.cell.component.httpsecurity.security.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel
public class AuthPermissionBaseReq
{
    // 获取得到具体权限信息
    @ApiModelProperty(value = "是否获取数据权限")
    private boolean dataPermission;
    @ApiModelProperty(value = "是否获取操作权限")
    private boolean operationPermission;
}
