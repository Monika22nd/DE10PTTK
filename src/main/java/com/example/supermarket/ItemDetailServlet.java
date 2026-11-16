package com.example.supermarket;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public final class ItemDetailServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!WebSupport.manager(request, response)) return;
        String start = request.getParameter("startDate"), end = request.getParameter("endDate");
        try {
            long itemId = Long.parseLong(request.getParameter("itemId"));
            var range = ItemStatisticsService6.parseRange(start, end);
            var service = WebSupport.services(request).statistics();
            var item = service.detail(itemId, range);
            if (item == null) { response.sendError(404, "Mặt hàng không tồn tại hoặc đã ngừng kinh doanh."); return; }
            request.setAttribute("item", item);
            try {
                request.setAttribute("transactions", service.transactions(itemId, range));
            } catch (SQLException e) {
                // The item and aggregate remain useful even if the related rows cannot be loaded.
                request.setAttribute("transactions", List.of());
                request.setAttribute("transactionWarning", "Không thể tải giao dịch liên quan. Vui lòng thử lại sau.");
            }
            request.setAttribute("startDate", start); request.setAttribute("endDate", end);
            WebSupport.page(request, response, "item-detail.jsp");
        } catch (IllegalArgumentException e) { response.sendError(400, "Mã mặt hàng hoặc khoảng ngày không hợp lệ."); }
        catch (SQLException e) { response.sendError(500, "Không thể tải danh sách giao dịch."); }
    }
}
