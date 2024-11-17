package asu.eng.gofund.view;

public class AuthView {
    public String showLoginPage() {
        return "loginPage";
    }

    public String showRegisterPage() {
        return "registerPage";
    }

    public String redirectToLogin(){
        return "redirect:/users/login";
    }
}