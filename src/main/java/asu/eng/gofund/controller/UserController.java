package asu.eng.gofund.controller;

import asu.eng.gofund.annotations.CurrentUser;
import asu.eng.gofund.controller.Login.*;
import asu.eng.gofund.model.User;
import asu.eng.gofund.model.UserType;
import asu.eng.gofund.repo.UserRepo;
import asu.eng.gofund.services.CookieService;
import asu.eng.gofund.services.JwtService;
import asu.eng.gofund.view.AuthView;
import asu.eng.gofund.view.CoreView;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.servlet.view.RedirectView;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Objects;

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


    AuthView authView = new AuthView();
    CoreView coreView = new CoreView();

    public static String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing input", e);
        }
    }

    @RequestMapping("/error")
    public String handleError(Model model) {
        model.addAttribute("error", "An unexpected error occurred");
        return coreView.showErrorPage();
    }

    @GetMapping("/login")
    public String login() {
        return authView.showLoginPage();
    }

    @GetMapping("/register")
    public String register() {
        return authView.showRegisterPage();
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
            return authView.showRegisterPage();
        }
        if(username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            model.addAttribute("error", "Please fill in all fields");
            return authView.showRegisterPage();
        }

        User createdUser = userRepo.save(new User(username, email, hash(password), strategy));

        if (createdUser != null) {
            return authView.redirectToLogin();
        } else {
            return authView.showRegisterPage();
        }
    }

    @PostMapping(value = "/login",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String loginUser(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("strategy") String strategy,
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
            loginManager.setStrategy(new UsernamePasswordLogin());
        }

        loginManager.setCredentials(username, hash(password));

        User user = loginManager.performLogin(response);

        if (user != null) {
            return coreView.redirectToHomePage();
        } else {
            model.addAttribute("error", "Invalid username or password");
            return authView.showLoginPage();
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        cookieService.removeAuthCookie(response);
        return coreView.redirectToHomePage();
    }

    @DeleteMapping("/{id}")
    public RedirectView deleteUser(@PathVariable Long id, @RequestParam("redirectURI") String redirectURI, @CurrentUser User currentUser) {
        User user = userRepo.findById(id).orElse(null);
        if (Objects.equals(currentUser.getUserType().getValue(), UserType.Admin.getValue())) {
            user.setDeleted(true);
            userRepo.save(user);
            return new RedirectView(redirectURI);
        }

        return coreView.redirectTo("/error");

    }

    @PutMapping("/{id}/type")
    public RedirectView updateUserType(@PathVariable Long id, @RequestParam("userType") String userType, @RequestParam("redirectURI") String redirectURI, @CurrentUser User currentUser) {
        User user = userRepo.findById(id).orElse(null);
        if (Objects.equals(currentUser.getUserType().getValue(), UserType.Admin.getValue())) {
            user.setUserType(UserType.valueOf(userType));
            userRepo.save(user);
            return coreView.redirectTo(redirectURI);
        }
        return coreView.redirectTo("/error");
    }
}
