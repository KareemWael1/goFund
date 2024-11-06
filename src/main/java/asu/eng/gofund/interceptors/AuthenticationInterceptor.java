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
public class AuthenticationInterceptor implements HandlerInterceptor {

    private final JwtService jwtService;
    private final UserRepo userRepo;
    private final CookieService cookieService;

    public AuthenticationInterceptor(JwtService jwtService, UserRepo userRepo, CookieService cookieService) {
        this.jwtService = jwtService;
        this.userRepo = userRepo;
        this.cookieService = cookieService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = cookieService.getAuthCookie(request);

        if (token != null && jwtService.isValidToken(token)) {
            Optional<User> user = userRepo.findById(jwtService.getUserIdFromToken(token));
            if (user.isPresent()) {
                request.setAttribute("user", user.get());
            } else {
                cookieService.removeAuthCookie(response);
            }
        }
        return true;
    }
}
