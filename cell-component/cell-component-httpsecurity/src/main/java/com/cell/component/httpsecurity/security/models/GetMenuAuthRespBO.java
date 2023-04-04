package com.cell.component.httpsecurity.security.models;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Collection;

@Data
@ApiModel
public class GetMenuAuthRespBO
{
    private Collection<MenuNode> items;

}
