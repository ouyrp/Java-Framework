package com.wusong.monitoring.loging;

import com.wusong.monitoring.MonitoringProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

/**
 * @author p14
 */
@Slf4j
public class LogFilter implements Filter {
    private MonitoringProperties monitoringProperties;
    private List<String> excludeAntPattern;

    public LogFilter(MonitoringProperties monitoringProperties) {
        this.monitoringProperties = monitoringProperties;
        excludeAntPattern = Arrays.asList(monitoringProperties.getExcludeAntPatterns().split(","));
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req= (HttpServletRequest) servletRequest;
        AntPathMatcher antPathMatcher=new AntPathMatcher();
        if(excludeAntPattern.stream().anyMatch(pattern->antPathMatcher.match(pattern,req.getRequestURI()))){
            filterChain.doFilter(servletRequest,servletResponse);
            return;
        }
        long start=System.currentTimeMillis();
        try{
            logBefore(servletRequest, servletResponse);
            filterChain.doFilter(servletRequest,servletResponse);
        } finally {
            logAfter(servletRequest,servletResponse,start);
        }
    }

    private void logBefore(ServletRequest servletRequest, ServletResponse servletResponse){
        HttpServletRequest req= (HttpServletRequest) servletRequest;
        if(!req.getRequestURI().contains("health")){
            final String url = req.getRequestURL().toString();
            final String method = req.getMethod();
            log.info("request {} {} from RemoteAddr={} x-real-ip={}",req.getMethod(),req.getRequestURI(),req.getRemoteAddr(),req.getHeader("x-real-ip"));
            if(log.isInfoEnabled()&&monitoringProperties.isLogHttpDetail()){
                logHeaders(req, url, method);
                logParameters(req, url, method);
            }
        }
    }

    private void logHeaders(HttpServletRequest request, String url, String method) {
        Enumeration<?> headers = request.getHeaderNames();
        StringBuilder builder=new StringBuilder();
        while (headers.hasMoreElements()) {
            String headerName = (String)headers.nextElement();
            builder.append(headerName).append("=").append(request.getHeader(headerName)).append(" ");
        }
        log.info("headers {}",builder);
    }

    private void logParameters(HttpServletRequest request, String url, String method) {
        Enumeration<?> paramNames = request.getParameterNames();
        StringBuilder builder=new StringBuilder();
        while (paramNames.hasMoreElements()) {
            String paramName = (String)paramNames.nextElement();
            builder.append(paramName).append("=").append(request.getParameter(paramName)).append(" ");
        }
        log.info("parameter {}",builder);
    }

    private void logAfter(ServletRequest servletRequest, ServletResponse servletResponse,long start){
        long cost=System.currentTimeMillis()-start;
        HttpServletRequest req= (HttpServletRequest) servletRequest;
        if(!req.getRequestURI().contains("health")||cost>1000){
            log.info("response {} {} {} in [{}] ms",req.getMethod(),req.getRequestURI(),((HttpServletResponse)servletResponse).getStatus(),cost);
        }
    }
}
