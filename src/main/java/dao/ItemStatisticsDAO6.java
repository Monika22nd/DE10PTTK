package dao;

import model.ItemCategory6;
import model.ItemStatistics6;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Thống kê mặt hàng theo doanh thu.
 * Quy tắc: chỉ tính giao dịch BÁN (có bản ghi trong tblCounterSale6 hoặc tblOnlineOrder6) ở trạng thái COMPLETED,
 * có ngày giao dịch trong [startDate, endDate] (tính trọn ngày kết thúc);
 * doanh thu = SUM(quantity x unitPrice) của dòng giao dịch (giá tại thời điểm bán).
 * Mặt hàng đã ngừng kinh doanh vẫn được thống kê và xem lịch sử.
 */
public class ItemStatisticsDAO6 extends DAO6 {

    /** Điều kiện lọc giao dịch bán hoàn tất trong khoảng ngày, dùng chung cho mọi truy vấn thống kê. */
    static final String SALE_FILTER =
            " t.status = 'COMPLETED'"
          + " AND (t.id IN (SELECT transactionId FROM tblCounterSale6) OR t.id IN (SELECT transactionId FROM tblOnlineOrder6))"
          + " AND t.transactionDate >= ? AND t.transactionDate < ? ";

    public ItemStatisticsDAO6(Connection con) {
        super(con);
    }

    /** Danh sách thống kê, sắp xếp tổng doanh thu giảm dần, bằng nhau thì theo mã mặt hàng tăng dần. */
    public ArrayList<ItemStatistics6> getItemStatistics(LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT i.*, k.name AS categoryName, k.description AS categoryDescription,"
                + " SUM(d.quantity) AS totalQuantity, SUM(d.quantity * d.unitPrice) AS totalRevenue"
                + " FROM tblTransaction6 t"
                + " JOIN tblTransactionDetail6 d ON d.transactionId = t.id"
                + " JOIN tblItem6 i ON i.id = d.itemId"
                + " JOIN tblItemCategory6 k ON k.id = i.categoryId"
                + " WHERE" + SALE_FILTER
                + " GROUP BY i.id, i.itemCode, i.name, i.unit, i.sellingPrice, i.quantityOnHand, i.status, i.description,"
                + " i.categoryId, k.name, k.description"
                + " ORDER BY totalRevenue DESC, i.itemCode ASC";
        ArrayList<ItemStatistics6> result = new ArrayList<>();
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            setRange(ps, 1, startDate, endDate);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) result.add(toStatistics(rs));
            }
        }
        return result;
    }

    /**
     * Thống kê của một mặt hàng trong khoảng ngày (dùng cho trang chi tiết).
     * @return ItemStatistics6 (tổng = 0 nếu không bán được) hoặc null nếu không có mặt hàng.
     */
    public ItemStatistics6 getItemStatisticsById(int itemId, LocalDate startDate, LocalDate endDate) throws SQLException {
        String sql = "SELECT i.*, k.name AS categoryName, k.description AS categoryDescription,"
                + " COALESCE(x.totalQuantity, 0) AS totalQuantity, COALESCE(x.totalRevenue, 0) AS totalRevenue"
                + " FROM tblItem6 i JOIN tblItemCategory6 k ON k.id = i.categoryId"
                + " LEFT JOIN (SELECT d.itemId, SUM(d.quantity) AS totalQuantity, SUM(d.quantity * d.unitPrice) AS totalRevenue"
                + "            FROM tblTransaction6 t JOIN tblTransactionDetail6 d ON d.transactionId = t.id"
                + "            WHERE" + SALE_FILTER + " GROUP BY d.itemId) x ON x.itemId = i.id"
                + " WHERE i.id = ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            setRange(ps, 1, startDate, endDate);
            ps.setInt(3, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? toStatistics(rs) : null;
            }
        }
    }

    static void setRange(PreparedStatement ps, int index, LocalDate startDate, LocalDate endDate) throws SQLException {
        ps.setTimestamp(index, Timestamp.valueOf(startDate.atStartOfDay()));
        ps.setTimestamp(index + 1, Timestamp.valueOf(endDate.plusDays(1).atStartOfDay()));
    }

    private static ItemStatistics6 toStatistics(ResultSet rs) throws SQLException {
        ItemStatistics6 s = new ItemStatistics6();
        s.setId(rs.getInt("id"));
        s.setItemCode(rs.getString("itemCode"));
        s.setName(rs.getString("name"));
        s.setUnit(rs.getString("unit"));
        s.setSellingPrice(rs.getBigDecimal("sellingPrice"));
        s.setQuantityOnHand(rs.getInt("quantityOnHand"));
        s.setStatus(rs.getString("status"));
        s.setDescription(rs.getString("description"));
        ItemCategory6 k = new ItemCategory6();
        k.setId(rs.getInt("categoryId"));
        k.setName(rs.getString("categoryName"));
        k.setDescription(rs.getString("categoryDescription"));
        s.setCategory(k);
        s.setTotalQuantity(rs.getInt("totalQuantity"));
        s.setTotalRevenue(rs.getBigDecimal("totalRevenue"));
        return s;
    }
}
