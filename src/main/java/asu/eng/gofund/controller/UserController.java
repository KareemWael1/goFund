package asu.eng.gofund.controller;


import asu.eng.gofund.model.User;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class UserController {

    public UserController() {
    }

    public int createUser(User user) {
        return user.saveUser();
    }

    public int updateUser(User user) {
        return user.updateUser();
    }

    public int deleteUser(Long id) {
        return User.deleteUserById(id);
    }

    public User getUser(Long id) {
        return User.getUserById(id);
    }

    public static User getUserByUsernameAndPassword(String username, String password) {
        return User.getUserByUsernameAndPassword(username, password);
    }

    public static User getUserByEmailAndPassword(String email, String password) {
        return User.getUserByEmailAndPassword(email, password);
    }

    public List<User> selectAllUsers() {
        return User.getAllUsers();
    }
}
