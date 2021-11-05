package com.cell.bee.loadbalance.model;

import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.util.Objects;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-12 17:31
 */
@Data
public class ServerCmdMetaInfo extends ServerMetaInfo
{
    private String module;

    public static ServerCmdMetaInfo fromServerMetaInfo(ServerMetaInfo info, String module)
    {
        ServerCmdMetaInfo ret = new ServerCmdMetaInfo();
        // FIXME
        BeanUtils.copyProperties(info, ret);
        ret.module = module;
        return ret;
    }

}
