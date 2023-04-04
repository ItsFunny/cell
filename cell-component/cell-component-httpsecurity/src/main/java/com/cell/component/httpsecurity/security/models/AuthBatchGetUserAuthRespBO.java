package com.cell.component.httpsecurity.security.models;

import lombok.Data;

import java.util.List;

@Data
public class AuthBatchGetUserAuthRespBO
{
    private List<UserNode> items;

}
