package com.cell.http.framework.filter;

import javax.servlet.*;
import java.io.IOException;
import java.util.List;

public class CellFilterChain implements Filter
{
    private List<CellFilter>filters;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException
    {
    }
}
