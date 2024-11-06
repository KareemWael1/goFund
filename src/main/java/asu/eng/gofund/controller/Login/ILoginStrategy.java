package asu.eng.gofund.controller.Login;

import asu.eng.gofund.model.User;
import asu.eng.gofund.services.JwtService;
import asu.eng.gofund.services.CookieService;

public interface ILoginStrategy {
    User login(String email, String password);
    String getStrategyName();
}
