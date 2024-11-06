package asu.eng.gofund.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class CookieService {
    private static final int MAX_AGE_SECONDS = 60 * 60 * 24 * 30;
    private static final String AUTH_COOKIE_NAME = "auth";

    public String getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        String res = cookies != null ? Arrays.stream(cookies)
                .filter(cookie -> cookieName.equals(cookie.getName()))
                .map(Cookie::getValue)
                .findFirst()
                .orElse(null) : null;
        return res;
    }

    public String getAuthCookie(HttpServletRequest request) {
        return getCookie(request, AUTH_COOKIE_NAME);
    }

    public void setCookie(HttpServletResponse response, String cookieName, String cookieValue) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setMaxAge(MAX_AGE_SECONDS);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public void setAuthCookie(HttpServletResponse response, String cookieValue) {
        setCookie(response, AUTH_COOKIE_NAME, cookieValue);
    }

    public void removeCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public void removeAuthCookie(HttpServletResponse response) {
        removeCookie(response, AUTH_COOKIE_NAME);
    }
}
