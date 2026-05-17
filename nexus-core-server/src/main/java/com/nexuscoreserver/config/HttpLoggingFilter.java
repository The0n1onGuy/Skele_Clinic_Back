package com.nexuscoreserver.config;

import com.nexuscoreserver.services.system.Systemlogservice;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class HttpLoggingFilter implements Filter {

    private final Systemlogservice logService;

    public HttpLoggingFilter(Systemlogservice logService) {
        this.logService = logService;
    }

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        long startTime = System.currentTimeMillis();

        try {
            chain.doFilter(request, response);
        } catch (Exception ex) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            throw ex;
        } finally {

            long duration = System.currentTimeMillis() - startTime;

            int statusCode = res.getStatus();
            String method = req.getMethod();
            String uri = req.getRequestURI();

            // Solo delega al servicio
            logService.log(statusCode, method, uri, duration);
        }
    }
}