package com.example.supermarket;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public final class ItemStatisticsServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!WebSupport.manager(request, response)) return;
        String start = request.getParameter("startDate");
        String end = request.getParameter("endDate");
        request.setAttribute("startDate", start == null ? LocalDate.now().minusDays(30).toString() : start);
        request.setAttribute("endDate", end == null ? LocalDate.now().toString() : end);
        request.setAttribute("rows", List.of());
        if (start != null || end != null) {
            try {
                var range = ItemStatisticsService6.parseRange(start, end);
                request.setAttribute("rows", WebSupport.services(request).statistics().list(range));
                request.setAttribute("searched", true);
            } catch (IllegalArgumentException e) { request.setAttribute("error", e.getMessage()); }
            catch (SQLException e) { request.setAttribute("error", "Không thể tải báo cáo. Vui lòng thử lại."); }
        }
        WebSupport.page(request, response, "item-statistics.jsp");
    }
}
