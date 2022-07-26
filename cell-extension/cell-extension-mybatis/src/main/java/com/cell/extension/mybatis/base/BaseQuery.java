package com.cell.extension.mybatis.base;

import java.time.LocalDate;
import java.util.List;

public class BaseQuery
{
    protected Integer pageIndex;
    protected Integer pageLimit;
    protected List<String> ascFields;
    protected List<String> descFields;
    protected LocalDate beginDate;
    protected LocalDate endDate;

    public Integer getPageIndex()
    {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex)
    {
        this.pageIndex = pageIndex;
    }

    public Integer getPageLimit()
    {
        return pageLimit;
    }

    public void setPageLimit(Integer pageLimit)
    {
        this.pageLimit = pageLimit;
    }

    public Integer getOffset()
    {
        if (pageIndex == null || pageLimit == null)
        {
            return 0;
        }
        return pageIndex * pageLimit;
    }

    public List<String> getAscFields()
    {
        return ascFields;
    }

    public void setAscFields(List<String> ascFields)
    {
        this.ascFields = ascFields;
    }

    public List<String> getDescFields()
    {
        return descFields;
    }

    public void setDescFields(List<String> descFields)
    {
        this.descFields = descFields;
    }

    public LocalDate getBeginDate()
    {
        return beginDate;
    }

    public void setBeginDate(LocalDate beginDate)
    {
        this.beginDate = beginDate;
    }

    public LocalDate getEndDate()
    {
        return endDate;
    }

    public void setEndDate(LocalDate endDate)
    {
        this.endDate = endDate;
    }

    @Override
    public String toString()
    {
        return "BaseQuery [pageIndex=" + pageIndex + ", pageLimit=" + pageLimit + ", ascFields=" + ascFields
                + ", descFields=" + descFields + ", beginDate=" + beginDate + ", endDate=" + endDate + "]";
    }

}
