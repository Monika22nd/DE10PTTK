package dao;

import model.CounterSale6;
import model.Item6;
import model.OnlineOrder6;
import model.Transaction6;
import model.TransactionDetail6;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/** Truy xuất giao dịch (tblTransaction6 và các bảng lớp con). */
public class TransactionDAO6 extends DAO6 {

    public TransactionDAO6(Connection con) {
        super(con);
    }

    /**
     * Các giao dịch bán hoàn tất trong khoảng ngày có chứa mặt hàng itemId (cùng bộ lọc với thống kê).
     * Mỗi Transaction6 (CounterSale6 hoặc OnlineOrder6) chỉ chứa các dòng TransactionDetail6 của mặt hàng đó.
     * Sắp xếp ngày giao dịch giảm dần, rồi mã giao dịch tăng dần.
     */
    public ArrayList<Transaction6> getTransactionsOfItem(int itemId, LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT t.id, t.transactionCode, t.transactionDate, t.status, t.note,"
                + " CASE WHEN t.id IN (SELECT transactionId FROM tblCounterSale6) THEN 'COUNTER_SALE' ELSE 'ONLINE_ORDER' END AS saleType,"
                + " d.id AS detailId, d.quantity, d.unitPrice, i.id AS itemId, i.itemCode, i.name AS itemName"
                + " FROM tblTransaction6 t"
                + " JOIN tblTransactionDetail6 d ON d.transactionId = t.id"
                + " JOIN tblItem6 i ON i.id = d.itemId"
                + " WHERE" + ItemStatisticsDAO6.SALE_FILTER + " AND d.itemId = ?"
                + " ORDER BY t.transactionDate DESC, t.transactionCode ASC, d.id ASC";
        Map<Integer, Transaction6> byId = new LinkedHashMap<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ItemStatisticsDAO6.setRange(ps, 1, startDate, endDate);
            ps.setInt(3, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    Transaction6 t = byId.get(id);
                    if (t == null) {
                        t = "COUNTER_SALE".equals(rs.getString("saleType")) ? new CounterSale6() : new OnlineOrder6();
                        t.setId(id);
                        t.setTransactionCode(rs.getString("transactionCode"));
                        t.setTransactionDate(rs.getTimestamp("transactionDate").toLocalDateTime());
                        t.setStatus(rs.getString("status"));
                        t.setNote(rs.getString("note"));
                        byId.put(id, t);
                    }
                    Item6 item = new Item6();
                    item.setId(rs.getInt("itemId"));
                    item.setItemCode(rs.getString("itemCode"));
                    item.setName(rs.getString("itemName"));
                    TransactionDetail6 d = new TransactionDetail6();
                    d.setId(rs.getInt("detailId"));
                    d.setQuantity(rs.getInt("quantity"));
                    d.setUnitPrice(rs.getBigDecimal("unitPrice"));
                    d.setItem(item);
                    t.getDetails().add(d);
                }
            }
        }
        return new ArrayList<>(byId.values());
    }
}
