package dao;

import model.ItemStatistics6;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Kiểm thử đơn vị ItemStatisticsDAO6. Dữ liệu mẫu (so với hôm nay = T):
 * PN001 (T-5, phiếu nhập) ITEM001x20 - không tính; HD001 (T-2) ITEM001 2x180000, ITEM002 3x35000;
 * DH001 (T-1) ITEM001 1x175000, ITEM003 2x120000 (ITEM003 đã ngừng bán); DH002 (T-1, PENDING) ITEM002x100 - không tính;
 * HD002 (T 00:05) ITEM002 4x34000.
 */
class TestItemStatisticsDAO6 {
    private Connection con;
    private ItemStatisticsDAO6 dao;
    private final LocalDate today = LocalDate.now();

    @BeforeEach void setUp() throws Exception { con = TestSupport6.newDatabase(); dao = new ItemStatisticsDAO6(con); }
    @AfterEach void tearDown() throws Exception { con.close(); }

    private static void assertMoney(long expected, BigDecimal actual) { assertEquals(0, BigDecimal.valueOf(expected).compareTo(actual)); }

    @Test void getItemStatistics_tinhDung_vaSapXepDoanhThuGiamDan() throws Exception {
        List<ItemStatistics6> list = dao.getItemStatistics(today.minusDays(30), today);
        assertEquals(3, list.size());
        assertEquals("ITEM001", list.get(0).getItemCode());
        assertEquals(3, list.get(0).getTotalQuantity());          // phiếu nhập 20 không tính
        assertMoney(535000, list.get(0).getTotalRevenue());       // 2x180000 + 1x175000 (giá lịch sử)
        assertEquals("ITEM002", list.get(1).getItemCode());
        assertEquals(7, list.get(1).getTotalQuantity());          // đơn PENDING 100 không tính
        assertMoney(241000, list.get(1).getTotalRevenue());
        assertEquals("ITEM003", list.get(2).getItemCode());       // mặt hàng ngừng bán vẫn được thống kê
        assertEquals("Thực phẩm khô", list.get(2).getCategory().getName());
    }

    @Test void getItemStatistics_ngayKetThucDuocTinhTronNgay() throws Exception {
        List<ItemStatistics6> list = dao.getItemStatistics(today, today);
        assertEquals(1, list.size());
        assertEquals("ITEM002", list.get(0).getItemCode());
        assertMoney(136000, list.get(0).getTotalRevenue());
    }

    @Test void getItemStatistics_khongCoGiaoDich_traVeDanhSachRong() throws Exception {
        assertTrue(dao.getItemStatistics(today.minusDays(60), today.minusDays(40)).isEmpty());
        assertTrue(dao.getItemStatistics(today.minusDays(5), today.minusDays(5)).isEmpty()); // chỉ có phiếu nhập
    }

    @Test void getItemStatisticsById_coBan_khongBan_khongTonTai() throws Exception {
        List<ItemStatistics6> list = dao.getItemStatistics(today.minusDays(30), today);
        ItemStatistics6 rice = dao.getItemStatisticsById(list.get(0).getId(), today.minusDays(30), today);
        assertMoney(535000, rice.getTotalRevenue());
        assertEquals("Túi", rice.getUnit());
        ItemStatistics6 noSale = dao.getItemStatisticsById(list.get(0).getId(), today.minusDays(60), today.minusDays(40));
        assertNotNull(noSale);
        assertEquals(0, noSale.getTotalQuantity());
        assertNull(dao.getItemStatisticsById(99999, today.minusDays(30), today));
    }
}
