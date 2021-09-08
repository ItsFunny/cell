package com.cell.transport.model;

import lombok.Data;

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
    private List<ServerMetaReactor> reactors;

    @Data
    public static class ServerMetaReactor
    {
        private List<ServerMetaCmd> cmds;
    }

    @Data
    public static class ServerMetaCmd
    {

    }

    private String reactorGroup;


}
