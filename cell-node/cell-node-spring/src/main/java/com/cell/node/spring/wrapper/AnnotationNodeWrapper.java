package com.cell.node.spring.wrapper;

import lombok.Data;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-29 15:37
 */
@Data
public class AnnotationNodeWrapper
{
    private Object node;
    private boolean asBean;

    public AnnotationNodeWrapper(Object node, boolean asBean)
    {
        this.node = node;
        this.asBean = asBean;
    }
}
