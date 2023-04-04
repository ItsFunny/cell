package com.cell.component.httpsecurity.security.models;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

@Data
@ApiModel
public class MenuNode {
    private Integer permissionId;
    private Integer menuResourceId;
    private Integer parentMenuResourceId;
    private String menuName;
    private String menuUrl;
    @ApiModelProperty(value = "子菜单信息")
    private List<MenuNode> childItems;
    private List<OperationNode> operationItems;
}
