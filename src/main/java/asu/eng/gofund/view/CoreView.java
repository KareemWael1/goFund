package asu.eng.gofund.view;

import org.springframework.web.servlet.view.RedirectView;

public class CoreView {
    public String showHomePage() {
        return "homePage";
    }

    public String showErrorPage() {
        return "error";
    }

    public RedirectView redirectTo(String path) {
        return new RedirectView(path);
    }

    public String redirectToHomePage() {
        return "redirect:/";
    }

    public String redirectToCertainPath(String redirectUrl) {
        return "redirect:" + redirectUrl;
    }
}