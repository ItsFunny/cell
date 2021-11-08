package com.cell.base.common.models;

import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-07 07:36
 */
@Data
public class Triple<V1, V2, V3>
{
    private V1 v1;
    private V2 v2;
    private V3 v3;

    public Triple(V1 v1, V2 v2, V3 v3)
    {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }
}
