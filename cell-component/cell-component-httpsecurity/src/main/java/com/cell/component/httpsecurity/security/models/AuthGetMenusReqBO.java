package com.cell.component.httpsecurity.security.models;

import lombok.Data;

import java.util.List;

@Data
public class AuthGetMenusReqBO
{
    private List<Integer>permissionIdList;

}
