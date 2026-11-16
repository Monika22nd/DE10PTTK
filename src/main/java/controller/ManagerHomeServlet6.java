package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * GET /manager: giao diện chính của nhân viên quản lý (gdManagerHome6.jsp);
 * GET /reports: giao diện chọn loại báo cáo thống kê (gdStatisticsMenu6.jsp).
 */
public class ManagerHomeServlet6 extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!WebUtil6.requireManager(req, resp)) return;
        WebUtil6.forward(req, resp, "/reports".equals(req.getServletPath()) ? "gdStatisticsMenu6.jsp" : "gdManagerHome6.jsp");
    }
}
