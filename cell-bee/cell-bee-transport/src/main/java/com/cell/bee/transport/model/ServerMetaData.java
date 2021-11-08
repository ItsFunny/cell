package com.cell.bee.transport.model;

import com.cell.base.common.constants.ProtocolConstants;
import com.cell.base.common.exceptions.ProgramaException;
import com.cell.base.common.utils.JSONUtil;
import com.cell.base.common.utils.StringUtils;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-08 18:46
 */
@Data
public class ServerMetaData
{
    public static final String PROPERTY_NAME = "cmds";
    private List<ServerMetaReactor> reactors;

    private ServerExtraInfo extraInfo;

    @Data
    public static class ServerExtraInfo
    {
        private String domain;
        private byte type = ProtocolConstants.TYPE_HTTP;
        private Map<String, Object> extraInfo;
    }

    public ServerMetaData()
    {
        this.reactors = new ArrayList<>();
    }

    public static Map<String, String> toMetaData(ServerMetaData data)
    {
        Map<String, String> metadatas = new HashMap<>();
        String meta = JSONUtil.toJsonString(data);
        metadatas.put(ServerMetaData.PROPERTY_NAME, meta);
        return metadatas;
    }

    public static ServerMetaData fromMetaData(Map<String, String> data)
    {
        String metas = data.get(ServerMetaData.PROPERTY_NAME);
        if (StringUtils.isEmpty(metas))
        {
            // FIXME ? PANIC  OR FALL THROUGH
            throw new ProgramaException("asd");
        }
        return JSONUtil.json2Obj(metas, ServerMetaData.class);
    }

    @Data
    public static class ServerMetaReactor
    {
        private List<ServerMetaCmd> cmds;
    }

    @Data
    public static class ServerMetaCmd
    {
        // get/post/delete
        private byte method;
        private String protocol;
        private String module;
    }


}
