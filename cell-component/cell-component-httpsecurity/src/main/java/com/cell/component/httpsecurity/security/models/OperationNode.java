package com.cell.component.httpsecurity.security.models;

import lombok.Data;

@Data
public class OperationNode
{
    private Integer operationPermissionId;
    private String operationName;
    private String operationRule;
}
