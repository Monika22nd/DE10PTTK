package com.example.supermarket;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

public final class LoginServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WebSupport.page(request, response, "login.jsp");
    }

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (!WebSupport.validCsrf(request)) { response.sendError(403, "Biểu mẫu hết hạn; tải lại trang và thử lại."); return; }
        String username = request.getParameter("username") == null ? "" : request.getParameter("username");
        String password = request.getParameter("password") == null ? "" : request.getParameter("password");
        try {
            AuthService6.User user = WebSupport.services(request).auth().login(username, password);
            if (user != null) {
                request.getSession().invalidate();
                HttpSession session = request.getSession(true);
                session.setAttribute("userId", user.id());
                session.setAttribute("displayName", user.fullName());
                session.setAttribute("role", user.role());
                WebSupport.csrf(session);
                response.sendRedirect(request.getContextPath() + ("MANAGEMENT_STAFF".equals(user.role()) ? "/reports" : "/"));
                return;
            }
            request.setAttribute("error", "Sai tên đăng nhập hoặc mật khẩu.");
        } catch (SQLException e) { request.setAttribute("error", "Không thể kết nối cơ sở dữ liệu. Vui lòng thử lại."); }
        request.setAttribute("username", username);
        WebSupport.page(request, response, "login.jsp");
    }
}
