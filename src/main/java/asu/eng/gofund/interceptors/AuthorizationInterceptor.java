package asu.eng.gofund.interceptors;

import asu.eng.gofund.model.User;
import asu.eng.gofund.repo.UserRepo;
import asu.eng.gofund.services.CookieService;
import asu.eng.gofund.services.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;
    private final UserRepo userRepo;
    private final CookieService cookieService;

    public AuthorizationInterceptor(JwtService jwtService, UserRepo userRepo, CookieService cookieService) {
        this.jwtService = jwtService;
        this.userRepo = userRepo;
        this.cookieService = cookieService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getAttribute("user");
        if(user == null) {
            response.setStatus(HttpServletResponse.SC_FOUND);
            response.setHeader("Location", "/users/login");
            response.flushBuffer();
            return false;
        }
        return true;
    }
}
