package controller;

import dao.Database6;
import dao.UserDAO6;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.ManagementStaff6;
import model.User6;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/** Use case phụ Login 6 (được include bởi module thống kê): GET hiển thị gdLogin6.jsp, POST kiểm tra đăng nhập. */
public class LoginServlet6 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebUtil6.forward(req, resp, "gdLogin6.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if (!WebUtil6.validCsrf(req)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Biểu mẫu đã hết hạn, vui lòng tải lại trang.");
            return;
        }
        String username = WebUtil6.param(req, "username");
        String password = req.getParameter("password");
        try (Connection con = Database6.getConnection()) {
            User6 user = new UserDAO6(con).checkLogin(username, password);
            if (user != null) {
                req.getSession().invalidate();
                HttpSession session = req.getSession(true);
                session.setAttribute("user", user);
                WebUtil6.csrfToken(session);
                resp.sendRedirect(req.getContextPath() + (user instanceof ManagementStaff6 ? "/manager" : "/"));
                return;
            }
            req.setAttribute("error", "Sai tên đăng nhập hoặc mật khẩu.");
        } catch (SQLException e) {
            req.setAttribute("error", "Không kết nối được cơ sở dữ liệu.");
        }
        req.setAttribute("username", username);
        WebUtil6.forward(req, resp, "gdLogin6.jsp");
    }
}
