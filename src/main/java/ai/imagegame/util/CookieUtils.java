package ai.imagegame.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

public class CookieUtils {
    public static void addCookie(String cookieKey, String cookieValue, HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieKey, cookieValue);
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        cookie.setMaxAge(30 * 24 * 60 * 60); //30days
        response.addCookie(cookie);
    }

    public static void removeCookie(String cookieKey, HttpServletResponse response) {
        Cookie cookie = new Cookie(cookieKey, "");
        cookie.setHttpOnly(false);
        cookie.setPath("/");
        cookie.setMaxAge(0); //30days
        response.addCookie(cookie);
    }
}
