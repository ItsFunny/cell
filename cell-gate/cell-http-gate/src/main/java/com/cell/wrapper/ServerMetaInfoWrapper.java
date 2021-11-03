package com.cell.wrapper;

import com.cell.bee.loadbalance.model.ServerMetaInfo;
import lombok.Data;

import java.net.URI;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-21 15:31
 */
@Data
public class ServerMetaInfoWrapper
{
    private ServerMetaInfo metaInfo;
    private URI uri;
    private String method;
}
