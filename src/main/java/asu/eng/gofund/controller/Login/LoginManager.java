package asu.eng.gofund.controller.Login;

import asu.eng.gofund.model.User;
import asu.eng.gofund.services.CookieService;
import asu.eng.gofund.services.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class LoginManager {
    private ILoginStrategy loginStrategy;
    private String username;
    private String password;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final CookieService cookieService;

    public LoginManager(JwtService jwtService, CookieService cookieService) {
        this.jwtService = jwtService;
        this.cookieService = cookieService;
    }

    public void setCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public void setStrategy(ILoginStrategy loginStrategy) {
        this.loginStrategy = loginStrategy;
    }

    public User performLogin(HttpServletResponse response) {
        User user = loginStrategy.login(username, password);
        if (user != null) {
            String jwt = jwtService.generateToken(user, loginStrategy.getStrategyName());
            cookieService.setAuthCookie(response, jwt);
        }
        return user;
    }
}
