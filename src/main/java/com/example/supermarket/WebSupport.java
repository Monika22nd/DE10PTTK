package com.example.supermarket;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

final class WebSupport {
    private static final SecureRandom RANDOM = new SecureRandom();
    private WebSupport() {}

    static AppServices services(HttpServletRequest request) {
        return (AppServices) request.getServletContext().getAttribute("services");
    }

    static void page(HttpServletRequest request, HttpServletResponse response, String file) throws ServletException, IOException {
        response.setContentType("text/html; charset=UTF-8");
        request.setAttribute("csrf", csrf(request.getSession()));
        request.setAttribute("demoMode", Boolean.parseBoolean(System.getProperty("supermarket.demo", "false")));
        request.getRequestDispatcher("/WEB-INF/views/" + file).forward(request, response);
    }

    static String csrf(HttpSession session) {
        String token = (String) session.getAttribute("csrf");
        if (token == null) {
            byte[] bytes = new byte[32]; RANDOM.nextBytes(bytes);
            token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
            session.setAttribute("csrf", token);
        }
        return token;
    }

    static boolean validCsrf(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        String expected = session == null ? null : (String) session.getAttribute("csrf");
        String actual = request.getParameter("csrf");
        return expected != null && actual != null && MessageDigest.isEqual(expected.getBytes(StandardCharsets.US_ASCII), actual.getBytes(StandardCharsets.US_ASCII));
    }

    static boolean manager(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("userId") == null) {
            response.sendRedirect(request.getContextPath() + "/login"); return false;
        }
        if (!"MANAGEMENT_STAFF".equals(session.getAttribute("role"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Chỉ nhân viên quản lý được xem báo cáo."); return false;
        }
        response.setHeader("Cache-Control", "no-store");
        return true;
    }
}
