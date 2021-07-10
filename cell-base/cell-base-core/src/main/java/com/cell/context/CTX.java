package com.cell.context;

import lombok.Data;

import java.util.Map;

@Data
public class CTX
{
    // FIXME ,need list

    private Map<String, Object> data;

    public Map<String, Object> getData()
    {
        return data;
    }

    public void setData(Map<String, Object> data)
    {
        this.data = data;
    }


}
