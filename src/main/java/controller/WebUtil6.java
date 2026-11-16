package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.ManagementStaff6;
import model.User6;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/** Tiện ích dùng chung cho các Servlet: chuyển tới JSP, CSRF token, kiểm tra quyền nhân viên quản lý. */
final class WebUtil6 {
    private static final SecureRandom RANDOM = new SecureRandom();

    private WebUtil6() {}

    /** Chuyển tiếp tới trang JSP trong /WEB-INF/view. */
    static void forward(HttpServletRequest req, HttpServletResponse resp, String jsp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        req.setAttribute("csrf", csrfToken(req.getSession()));
        req.setAttribute("demoMode", Boolean.parseBoolean(System.getProperty("supermarket.demo", "false")));
        req.getRequestDispatcher("/WEB-INF/view/" + jsp).forward(req, resp);
    }

    static String csrfToken(HttpSession session) {
        String token = (String) session.getAttribute("csrf");
        if (token == null) {
            byte[] bytes = new byte[32];
            RANDOM.nextBytes(bytes);
            token = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
            session.setAttribute("csrf", token);
        }
        return token;
    }

    static boolean validCsrf(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        String expected = session == null ? null : (String) session.getAttribute("csrf");
        String actual = req.getParameter("csrf");
        return expected != null && actual != null
                && MessageDigest.isEqual(expected.getBytes(StandardCharsets.US_ASCII), actual.getBytes(StandardCharsets.US_ASCII));
    }

    /** Người dùng đang đăng nhập (đối tượng User6 lưu trong session) hoặc null. */
    static User6 currentUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        return session == null ? null : (User6) session.getAttribute("user");
    }

    /** Chỉ cho phép nhân viên quản lý; chưa đăng nhập thì chuyển tới trang đăng nhập. */
    static boolean requireManager(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User6 user = currentUser(req);
        if (user == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
            return false;
        }
        if (!(user instanceof ManagementStaff6)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Chỉ nhân viên quản lý được xem báo cáo thống kê.");
            return false;
        }
        resp.setHeader("Cache-Control", "no-store");
        return true;
    }

    static String param(HttpServletRequest req, String name) {
        String v = req.getParameter(name);
        return v == null ? "" : v.trim();
    }
}
