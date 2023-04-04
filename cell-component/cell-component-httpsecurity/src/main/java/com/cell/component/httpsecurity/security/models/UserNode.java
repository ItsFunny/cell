package com.cell.component.httpsecurity.security.models;

import lombok.Data;

import java.util.Collection;

@Data
public class UserNode
{
    private Integer userId;
    private GroupNode groupNode;
    private Collection<RoleNode> items;
}
