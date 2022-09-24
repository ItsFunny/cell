package com.cell.component.download.common.request;

import lombok.Data;

@Data
public class CatchUpRequest
{
    private long from;
    private long to;

    public CatchUpRequest(long from, long to)
    {
        this.from = from;
        this.to = to;
    }
}
