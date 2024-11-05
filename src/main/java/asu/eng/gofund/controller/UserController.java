package asu.eng.gofund.controller;


import asu.eng.gofund.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/users")
public class UserController {

    @GetMapping()
    public List<User> getAllUsers() {
        List<User> users = User.getAllUsers();
        for (User user : users) {
            System.out.println(user.getId() + " " + user.getUsername() + " " + user.getEmail() + " " + user.getPassword());
        }
        return users;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        User user = User.getUserById(id);
        System.out.println(user.getId() + " " + user.getUsername() + " " + user.getEmail() + " " + user.getPassword());
        return user;
    }

    @PostMapping()
    public String createUser(@RequestBody User user) {
        int result = user.saveUser();
        if (result == 1) {
            return "User created successfully";
        } else {
            return "User creation failed";
        }
    }

    @PutMapping()
    public String updateUser(@RequestBody User user) {
        int result = user.updateUser();
        if (result == 1) {
            return "User updated successfully";
        } else {
            return "User update failed";
        }
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        int result = User.deleteUserById(id);
        if (result == 1) {
            return "User deleted successfully";
        } else {
            return "User deletion failed";
        }
    }

    public static User getUserByUsernameAndPassword(String username, String password) {
        return User.getUserByUsernameAndPassword(username, password);
    }

    public static User getUserByEmailAndPassword(String email, String password) {
        return User.getUserByEmailAndPassword(email, password);
    }

}
