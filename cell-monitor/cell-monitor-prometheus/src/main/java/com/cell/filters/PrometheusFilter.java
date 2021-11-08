//package com.cell.filters;
//
//import com.cell.base.core.log.LOG;
//import com.cell.base.common.models.Module;
//import com.cell.monitor.IMonitor;
//import lombok.Data;
//import org.springframework.stereotype.Component;
//
//import javax.servlet.*;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import java.io.IOException;
//
///**
// * @author Charlie
// * @When
// * @Description
// * @Detail
// * @Attention:
// * @Date 创建时间：2021-09-20 07:50
// */
//@Component
//@WebFilter(urlPatterns = "/actuator/prometheus", filterName = "PrometheusMetricsFilter")
//@Data
//public class PrometheusFilter implements Filter
//{
//    private static final String prometheusUrl = "/actuator/prometheus";
//    private IMonitor monitor;
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException
//    {
//        if (!(request instanceof HttpServletRequest))
//        {
//            chain.doFilter(request, response);
//            return;
//        }
//
//        final String requestURI = ((HttpServletRequest) request).getRequestURI();
//        if (!prometheusUrl.equals(requestURI))
//        {
//            chain.doFilter(request, response);
//            return;
//        }
//        LOG.info(Module.STATISTIC, "Start update metrics");
//        if (monitor != null)
//        {
//            monitor.updateMetrics();
//        }
//        chain.doFilter(request, response);
//    }
//}
