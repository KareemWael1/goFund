package asu.eng.gofund.interceptors;

import asu.eng.gofund.proxies.AuthenticationProxy;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final AuthenticationProxy authenticationProxy;

    public AuthenticationInterceptor(AuthenticationProxy authenticationProxy) {
        this.authenticationProxy = authenticationProxy;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return this.authenticationProxy.authenticate(request, response);
    }
}
