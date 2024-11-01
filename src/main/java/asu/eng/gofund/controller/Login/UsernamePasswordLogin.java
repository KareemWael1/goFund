package asu.eng.gofund.controller.Login;

import asu.eng.gofund.controller.UserController;

import java.util.Map;

import static asu.eng.gofund.util.HashUtil.hash;

public class UsernamePasswordLogin implements ILoginStrategy{
    private String username;
    private String password;

    @Override
    public boolean login(Map<String, String> credentials) {
        username = credentials.get("username");
        password = hash(credentials.get("password"));
        return UserController.getUserByUsernameAndPassword(username, password) != null;
    }
}
