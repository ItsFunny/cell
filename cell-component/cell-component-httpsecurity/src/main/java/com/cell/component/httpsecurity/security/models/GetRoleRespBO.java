package com.cell.component.httpsecurity.security.models;

import lombok.Data;

import java.util.Collection;

@Data
public class GetRoleRespBO
{
    private Collection<RoleNode> items;
}
