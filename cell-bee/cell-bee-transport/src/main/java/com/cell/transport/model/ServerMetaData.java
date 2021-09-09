package com.cell.transport.model;

import com.cell.utils.JSONUtil;
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

    public ServerMetaData()
    {
        this.reactors = new ArrayList<>();
    }

    public static Map<String,String> toMetaData(ServerMetaData data){
        Map<String,String> metadatas=new HashMap<>();
        String meta = JSONUtil.toJsonString(data);
        metadatas.put(ServerMetaData.PROPERTY_NAME, meta);
        return metadatas;
    }

    @Data
    public static class ServerMetaReactor
    {
        private List<ServerMetaCmd> cmds;
    }

    @Data
    public static class ServerMetaCmd
    {
        private String uri;
    }


}
