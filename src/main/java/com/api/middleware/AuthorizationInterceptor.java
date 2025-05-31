package com.api.middleware;

import com.api.util.Helper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String method = request.getMethod();
        String uri = request.getRequestURI();

        if ((method.equals("PUT") || method.equals("DELETE")) //&& uri.matches(".*/[^/]+$")
                ) {
            String id = uri.substring(uri.lastIndexOf('/') + 1);

            if (!Helper.isOwner(id, request)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType("application/json");
                response.getWriter().write(String.format(
                        "{\"code\":403,\"message\":\"Access denied: Insufficient permissions\",\"path\":\"%s\"}",
                        uri
                ));
                return false;
            }
        }

        return true;
    }
}
