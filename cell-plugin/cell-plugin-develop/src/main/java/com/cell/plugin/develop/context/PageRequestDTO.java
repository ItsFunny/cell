package com.cell.plugin.develop.context;

import lombok.Data;

@Data
public class PageRequestDTO implements IValidBasic
{
    private Integer pageIndex;
    private Integer pageSize;

    @Override
    public void validBasic(CellContext context) throws IllegalArgumentException
    {
        if (this.pageIndex == null || this.pageIndex <= 0)
        {
            this.pageIndex=1;
        }
        if (this.pageSize==null || this.pageSize<=0){
            this.pageSize=100000;
        }
    }
    public void internalSearch(){
        this.pageIndex=1;
        this.pageSize=Integer.MAX_VALUE;
    }
}
