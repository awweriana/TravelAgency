package org.example.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("AuthFilter INITIALIZED");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        System.out.println("FILTER: " + req.getRequestURI());

        // тест
        chain.doFilter(request, response);
        return;
    }

    @Override
    public void destroy() {
        System.out.println("AuthFilter DESTROYED");
    }
}