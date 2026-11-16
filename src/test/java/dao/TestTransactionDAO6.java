package dao;

import model.CounterSale6;
import model.ItemStatistics6;
import model.OnlineOrder6;
import model.Transaction6;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/** Kiểm thử đơn vị TransactionDAO6.getTransactionsOfItem(). */
class TestTransactionDAO6 {
    private Connection con;
    private final LocalDate today = LocalDate.now();

    @BeforeEach void setUp() throws Exception { con = TestSupport6.newDatabase(); }
    @AfterEach void tearDown() throws Exception { con.close(); }

    private int idOf(String code) throws Exception {
        for (ItemStatistics6 s : new ItemStatisticsDAO6(con).getItemStatistics(today.minusDays(30), today))
            if (s.getItemCode().equals(code)) return s.getId();
        throw new AssertionError("Không thấy " + code);
    }

    @Test void getTransactionsOfItem_chiLayGiaoDichBanHoanTat_moiNhatTruoc() throws Exception {
        List<Transaction6> list = new TransactionDAO6(con).getTransactionsOfItem(idOf("ITEM001"), today.minusDays(30), today);
        assertEquals(2, list.size());                                  // PN001 (phiếu nhập) bị loại
        assertEquals("DH001", list.get(0).getTransactionCode());
        assertTrue(list.get(0) instanceof OnlineOrder6);
        assertEquals("HD001", list.get(1).getTransactionCode());
        assertTrue(list.get(1) instanceof CounterSale6);
        assertEquals(1, list.get(1).getDetails().size());              // chỉ dòng của ITEM001
        assertEquals(0, BigDecimal.valueOf(360000).compareTo(list.get(1).getDetails().get(0).getAmount()));
    }

    @Test void getTransactionsOfItem_loaiDonPending() throws Exception {
        List<Transaction6> list = new TransactionDAO6(con).getTransactionsOfItem(idOf("ITEM002"), today.minusDays(30), today);
        assertEquals(2, list.size());
        for (Transaction6 t : list) assertNotEquals("DH002", t.getTransactionCode());
    }

    @Test void getTransactionsOfItem_ngoaiKhoangNgay_rong() throws Exception {
        assertTrue(new TransactionDAO6(con).getTransactionsOfItem(idOf("ITEM001"), today.minusDays(60), today.minusDays(40)).isEmpty());
    }
}
