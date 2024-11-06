package asu.eng.gofund.controller.Login;

import asu.eng.gofund.model.User;
import org.springframework.stereotype.Component;

@Component
public class FacebookLogin implements ILoginStrategy {
    @Override
    public String getStrategyName() {
        return "facebook";
    }

    @Override
    public User login(String email, String password) {
        User user = User.getUserByUsernameAndPassword(email, password);
        if(user != null && user.getLoginStrategy().equals(getStrategyName())) {
            return user;
        }
        return null;
    }
}
