package asu.eng.gofund.view;

import asu.eng.gofund.controller.UserController;
import asu.eng.gofund.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserView {

    @Autowired
    private UserController userController;

    @GetMapping()
    public List<User> getAllUsers() {
        List<User> users = userController.selectAllUsers();
        for (User user : users) {
            System.out.println(user.getId() + " " + user.getUsername() + " " + user.getEmail() + " " + user.getPassword());
        }
        return users;
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        User user = userController.getUser(id);
        System.out.println(user.getId() + " " + user.getUsername() + " " + user.getEmail() + " " + user.getPassword());
        return user;
    }

    @PostMapping()
    public String createUser(@RequestBody User user) {
        int result = userController.createUser(user);
        if (result == 1) {
            return "User created successfully";
        } else {
            return "User creation failed";
        }
    }

    @PutMapping()
    public String updateUser(@RequestBody User user) {
        int result = userController.updateUser(user);
        if (result == 1) {
            return "User updated successfully";
        } else {
            return "User update failed";
        }
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable Long id) {
        int result = userController.deleteUser(id);
        if (result == 1) {
            return "User deleted successfully";
        } else {
            return "User deletion failed";
        }
    }
}
