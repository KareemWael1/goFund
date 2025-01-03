package asu.eng.gofund.proxies;

import asu.eng.gofund.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationProxy {

    public boolean authorize(HttpServletRequest request, HttpServletResponse response) throws Exception {
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