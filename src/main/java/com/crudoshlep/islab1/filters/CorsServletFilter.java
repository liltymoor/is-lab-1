package com.crudoshlep.islab1.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class CorsServletFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // ничего не делаем
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Получаем Origin запроса
        String origin = httpRequest.getHeader("Origin");
        if (origin == null) {
            origin = "*";
        }

        // Заголовки CORS
        httpResponse.setHeader("Access-Control-Allow-Origin", origin);
        httpResponse.setHeader("Vary", "Origin"); // чтобы прокси кешировали по Origin
        httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpResponse.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization");
        httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
        httpResponse.setHeader("Access-Control-Max-Age", "3600"); // кэш preflight
        httpResponse.setHeader("Access-Control-Expose-Headers", "X-Total-Count, X-Offset, X-Limit");

        // Если OPTIONS, сразу возвращаем 200
        if ("OPTIONS".equalsIgnoreCase(httpRequest.getMethod())) {
            httpResponse.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // ничего не делаем
    }
}
