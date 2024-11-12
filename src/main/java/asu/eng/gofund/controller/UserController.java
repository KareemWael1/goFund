package asu.eng.gofund.controller;

import asu.eng.gofund.controller.Login.*;
import asu.eng.gofund.model.Observer;
import asu.eng.gofund.model.User;
import asu.eng.gofund.repo.UserRepo;
import asu.eng.gofund.services.CookieService;
import asu.eng.gofund.services.JwtService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/users")
public class UserController implements ErrorController {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CookieService cookieService;

    @Autowired
    private LoginManager loginManager;

    @RequestMapping("/error")
    public String handleError(Model model) {
        model.addAttribute("error", "An unexpected error occurred");
        return "error";
    }

    @GetMapping("/login")
    public String login() {
        return "loginPage";
    }

    @GetMapping("/register")
    public String register() {
        return "registerPage";
    }

    @PostMapping("/register")
    public String registerUser(
            @RequestParam String username,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            @RequestParam String strategy,
            Model model
    ) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match");
            return "registerPage";
        }
        if(username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            model.addAttribute("error", "Please fill in all fields");
            return "registerPage";
        }

        User createdUser = userRepo.save(new User(username, email, password, strategy));

        if (createdUser != null) {
            return "redirect:/users/login";
        } else {
            return "registerPage";
        }
    }

    @PostMapping("/login")
    public String loginUser(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String strategy,
            Model model,
            HttpServletResponse response
    ) {
        if(strategy.equals("google")) {
            loginManager.setStrategy(new GoogleLogin());
        } else if(strategy.equals("facebook")) {
            loginManager.setStrategy(new FacebookLogin());
        } else if(strategy.equals("github")) {
            loginManager.setStrategy(new GithubLogin());
        } else {
            loginManager.setStrategy(new EmailPasswordLogin());
        }

        loginManager.setCredentials(username, password);

        User user = loginManager.performLogin(response);

        if (user != null) {
            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid username or password");
            return "loginPage";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        cookieService.removeAuthCookie(response);
        return "redirect:/";
    }


}
