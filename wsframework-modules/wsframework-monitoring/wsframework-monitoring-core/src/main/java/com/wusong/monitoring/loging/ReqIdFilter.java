package com.wusong.monitoring.loging;

import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author p14
 */
public class ReqIdFilter implements Filter {
    private AtomicLong counter=new AtomicLong();
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            MDC.put("reqId",getRequestId(request));
            chain.doFilter(request, response);
        }finally {
            MDC.clear();
        }
    }
    private String getRequestId(ServletRequest request){
        String id=((HttpServletRequest)request).getHeader("x-request-id");
        if(id==null){
            id="req-"+counter.incrementAndGet();
        }
        return id;
    }
}
