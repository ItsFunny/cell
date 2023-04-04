package com.cell.component.httpsecurity.security.models;

import lombok.Data;

@Data
public class GetUserAuthReqBO extends AuthPermissionBaseReq
{
    private Integer userId;
}
