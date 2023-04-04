package com.cell.component.httpsecurity.security.models;

import lombok.Data;

import java.util.Collection;

@Data
public class GetMenuAuthReqBO
{
    private Collection<Integer> permissionIdList;
    private boolean operationPermission;
}
