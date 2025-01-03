package asu.eng.gofund.interceptors;

import asu.eng.gofund.proxies.AuthorizationProxy;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final AuthorizationProxy authorizationProxy;

    public AuthorizationInterceptor(AuthorizationProxy authorizationProxy) {
        this.authorizationProxy = authorizationProxy;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return this.authorizationProxy.authorize(request, response);
    }
}
