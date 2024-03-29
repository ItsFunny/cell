package com.cell.base.core.protocol;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-08-31 15:20
 */
@Data
@Builder
public class ContextResponseWrapper
{
    private long status;
    private String msg;
    private Throwable exception;
    private ICommand cmd;
    private Object ret;
    private Map<String, String> headers;
    // 从哪里调用了该 wp
    private String from;

    private Object other;
}
