package asu.eng.gofund.controller.Login;

import asu.eng.gofund.controller.UserController;

import java.util.Map;

public class EmailPasswordLogin implements ILoginStrategy {
    private String email;
    private String password;

    @Override
    public boolean login(Map<String, String> credentials) {
//        email = credentials.get("email");
//        password = credentials.get("password");
//        return UserController.getUserByEmailAndPassword(email, password) != null;
        return false;
    }
}
