package com.wusong.crypt.web;

import com.wusong.crypt.common.AuthConstants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * 缓存 request body，以便多次读取或者解密。
 * @author p14
 */
public class SignatureRequestWrapper extends HttpServletRequestWrapper {

    public static class HttpServletFilter implements Filter{
        @Override
        public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
            if(servletRequest instanceof HttpServletRequest&&((HttpServletRequest) servletRequest).getHeader(AuthConstants.HEADER_SIGN_BODY)!=null){
                filterChain.doFilter(new SignatureRequestWrapper((HttpServletRequest) servletRequest),servletResponse);
            }else {
                filterChain.doFilter(servletRequest,servletResponse);
            }
        }
    }

    public SignatureRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        if(cacheBody()){
            StringBuilder stringBuilder = new StringBuilder();
            ServletInputStream inputStream = request.getInputStream();
            this.encoding=request.getCharacterEncoding()==null? StandardCharsets.UTF_8.name():request.getCharacterEncoding();
            if (inputStream != null) {
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,this.encoding))) {
                    char[] buffer = new char[128];
                    int read;
                    while ((read = bufferedReader.read(buffer)) > 0) {
                        stringBuilder.append(buffer, 0, read);
                    }
                }
            }
            this.body=stringBuilder.toString();
        }
    }

    private String encoding;
    private String body;

    public String getBody() {
        return body;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        if(cacheBody()){
            ByteArrayInputStream byteArrayInputStream=new ByteArrayInputStream(body.getBytes(this.encoding));
            return new ServletInputStream() {
                @Override
                public boolean isFinished() {
                    return false;
                }

                @Override
                public boolean isReady() {
                    return false;
                }

                @Override
                public void setReadListener(ReadListener readListener) {
                }

                @Override
                public int read() throws IOException {
                    return byteArrayInputStream.read();
                }
            };
        }else {
            return super.getInputStream();
        }
    }

    public final boolean cacheBody() {
        return true;
    }
}
