package com.cell.component.httpsecurity.security.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class AddUserRespBO
{
    @JsonSerialize(using = ToStringSerializer.class)
    private Integer userId;
}
