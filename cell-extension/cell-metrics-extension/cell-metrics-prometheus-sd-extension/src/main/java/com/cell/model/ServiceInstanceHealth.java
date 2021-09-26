package com.cell.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

/**
 * @author Charlie
 * @When
 * @Description
 * @Detail
 * @Attention:
 * @Date 创建时间：2021-09-26 21:45
 */
@Data
@ToString
@Builder
public class ServiceInstanceHealth
{
    @JsonProperty("Node")
    private Node node;

    @JsonProperty("Service")
    private Service service;

    @JsonProperty("Checks")
    private List<Check> checks;

    @Getter
    @Builder
    public static class Node
    {

        @JsonProperty("ID")
        private String id;

        @JsonProperty("Address")
        //nacos默认注册为内网IP
        private String address;

        @JsonProperty("Datacenter")
        private String dataCenter;
    }

    @Getter
    @Builder
    public static class Service
    {

        @JsonProperty("ID")
        private String id;

        @JsonProperty("Service")
        private String service;

        @JsonProperty("Port")
        private int port;
    }

    @Getter
    @Builder
    public static class Check
    {

        @JsonProperty("Node")
        private String node;

        @JsonProperty("CheckID")
        private String checkID;

        @JsonProperty("Name")
        private String name;

        @JsonProperty("Status")
        private String status;
    }

}
