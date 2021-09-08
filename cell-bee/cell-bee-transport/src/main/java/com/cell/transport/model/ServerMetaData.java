package com.cell.transport.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

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
