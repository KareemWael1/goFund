package asu.eng.gofund.controller;

import asu.eng.gofund.annotations.CurrentUser;
import asu.eng.gofund.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class Home {
    @GetMapping("")
    public String home(
            HttpServletRequest request,
            Model model,
            @CurrentUser User user
    ) {
        if(user != null) {
            model.addAttribute("user", user);
        }
        return "homePage";
    }
}