package com.cell.component.download.common.request;

import lombok.Data;

@Data
public class FetchBlockRequest
{
    private long from;
    private long to;

    public FetchBlockRequest(long from, long to)
    {
        this.from = from;
        this.to = to;
    }
}
