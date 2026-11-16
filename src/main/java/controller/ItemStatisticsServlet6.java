package controller;

import dao.Database6;
import dao.ItemStatisticsDAO6;
import dao.TransactionDAO6;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.ItemStatistics6;
import model.Transaction6;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Controller module View Item Statistics by Revenue 6.
 * GET /reports/items: chọn khoảng ngày + danh sách thống kê (gdItemStatistics6.jsp);
 * GET /reports/items/detail: chi tiết mặt hàng và giao dịch liên quan (gdItemDetail6.jsp).
 */
public class ItemStatisticsServlet6 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!WebUtil6.requireManager(req, resp)) return;
        if ("/reports/items/detail".equals(req.getServletPath())) showItemDetail(req, resp);
        else showStatistics(req, resp);
    }

    /** Hiển thị form chọn ngày; nếu đã nhập ngày thì kiểm tra và lấy danh sách thống kê. */
    private void showStatistics(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String start = WebUtil6.param(req, "startDate");
        String end = WebUtil6.param(req, "endDate");
        req.setAttribute("startDate", start);
        req.setAttribute("endDate", end);
        if (req.getParameter("startDate") != null || req.getParameter("endDate") != null) {
            String error = validateDateRange(start, end);
            if (error != null) {
                req.setAttribute("error", error);
            } else {
                try (Connection con = Database6.getConnection()) {
                    ArrayList<ItemStatistics6> list = new ItemStatisticsDAO6(con)
                            .getItemStatistics(LocalDate.parse(start), LocalDate.parse(end));
                    req.setAttribute("statistics", list);
                    req.setAttribute("searched", true);
                } catch (SQLException e) {
                    req.setAttribute("error", "Không thể tải dữ liệu thống kê. Vui lòng thử lại.");
                }
            }
        }
        if (req.getAttribute("message") == null && req.getParameter("message") != null)
            req.setAttribute("message", req.getParameter("message"));
        WebUtil6.forward(req, resp, "gdItemStatistics6.jsp");
    }

    /** Hiển thị chi tiết một mặt hàng với cùng khoảng ngày và danh sách giao dịch bán liên quan. */
    private void showItemDetail(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String start = WebUtil6.param(req, "startDate");
        String end = WebUtil6.param(req, "endDate");
        String error = validateDateRange(start, end);
        int itemId;
        try {
            itemId = Integer.parseInt(WebUtil6.param(req, "itemId"));
        } catch (NumberFormatException e) {
            itemId = -1;
        }
        if (error != null || itemId < 0) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Mặt hàng hoặc khoảng ngày không hợp lệ.");
            return;
        }
        LocalDate s = LocalDate.parse(start), e = LocalDate.parse(end);
        try (Connection con = Database6.getConnection()) {
            ItemStatistics6 item = new ItemStatisticsDAO6(con).getItemStatisticsById(itemId, s, e);
            if (item == null) {
                req.setAttribute("startDate", start);
                req.setAttribute("endDate", end);
                req.setAttribute("statistics", new ItemStatisticsDAO6(con).getItemStatistics(s, e));
                req.setAttribute("searched", true);
                req.setAttribute("error", "Không tìm thấy mặt hàng đã chọn. Danh sách đã được làm mới.");
                WebUtil6.forward(req, resp, "gdItemStatistics6.jsp");
                return;
            }
            List<Transaction6> transactions;
            try {
                transactions = new TransactionDAO6(con).getTransactionsOfItem(itemId, s, e);
            } catch (SQLException ex) {
                transactions = new ArrayList<>();
                req.setAttribute("warning", "Không tải được danh sách giao dịch liên quan.");
            }
            req.setAttribute("item", item);
            req.setAttribute("transactions", transactions);
            req.setAttribute("startDate", start);
            req.setAttribute("endDate", end);
            WebUtil6.forward(req, resp, "gdItemDetail6.jsp");
        } catch (SQLException ex) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Không thể tải thông tin mặt hàng.");
        }
    }

    /** @return thông báo lỗi hoặc null nếu khoảng ngày hợp lệ (đủ hai ngày, đúng định dạng, start ≤ end). */
    static String validateDateRange(String start, String end) {
        if (start.isEmpty() || end.isEmpty()) return "Vui lòng chọn đủ ngày bắt đầu và ngày kết thúc.";
        try {
            if (LocalDate.parse(start).isAfter(LocalDate.parse(end)))
                return "Ngày bắt đầu không được lớn hơn ngày kết thúc.";
            return null;
        } catch (DateTimeParseException e) {
            return "Ngày không đúng định dạng.";
        }
    }
}
