package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Trang chủ của khách hàng/khách vãng lai: gdHome6.jsp (menu Đăng ký thành viên, Đăng nhập). */
public class HomeServlet6 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        WebUtil6.forward(req, resp, "gdHome6.jsp");
    }
}
