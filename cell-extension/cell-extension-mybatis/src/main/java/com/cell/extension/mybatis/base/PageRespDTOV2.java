package com.cell.extension.mybatis.base;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cell.base.common.decorators.IDecorator;
import lombok.Data;

import java.util.List;

@Data
public abstract class PageRespDTOV2<F, T>
{
    private List<T> items;
    private Integer totalCount;
    private Integer pages;
    private Integer size;

    public void fromPage(IPage<F> page, IDecorator<T>...consumers)
    {
        this.totalCount = Math.toIntExact(page.getTotal());
        this.items = this.conv(page.getRecords(),consumers);
        this.pages = Math.toIntExact(page.getPages());
        this.size = Math.toIntExact(page.getSize());
    }

    public void fromSame(Integer pageIndex,Integer pageSize,Integer totalCount,List<F>f ){
        IPage<F>page=new Page<>(pageIndex,pageSize,totalCount);
        page.setRecords(f);
        this.fromPage(page);
    }

    public void setBasePage(IPage<F> page)
    {
        this.totalCount = Math.toIntExact(page.getTotal());
        this.pages = Math.toIntExact(page.getPages());
        this.size = Math.toIntExact(page.getSize());
    }

    protected abstract List<T> conv(List<F> f, IDecorator<T> ...consumers);
}
