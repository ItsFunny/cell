package com.cell.models;

import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-11-07 07:38
 */
@Data
public class Quadruple<V1, V2, V3, V4>
{
    private V1 v1;
    private V2 v2;
    private V3 v3;
    private V4 v4;

    public Quadruple(V1 v1, V2 v2, V3 v3, V4 v4)
    {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
        this.v4 = v4;
    }
}
