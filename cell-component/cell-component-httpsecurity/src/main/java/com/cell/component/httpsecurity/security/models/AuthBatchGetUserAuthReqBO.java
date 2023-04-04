package com.cell.component.httpsecurity.security.models;


import lombok.Data;

import java.util.List;

@Data
public class AuthBatchGetUserAuthReqBO extends  AuthPermissionBaseReq
{
    private List<Integer> userIdList;

}
