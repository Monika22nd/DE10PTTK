package dao;

import model.Customer6;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/** Kiểm thử đơn vị CustomerDAO6.addCustomer(): lưu nguyên tử tblUser6 + tblCustomer6. */
class TestCustomerDAO6 {
    private Connection con;
    private CustomerDAO6 dao;

    @BeforeEach void setUp() throws Exception { con = TestSupport6.newDatabase(); dao = new CustomerDAO6(con); }
    @AfterEach void tearDown() throws Exception { con.close(); }

    private Customer6 newCustomer(String username, String email, String phone) {
        Customer6 c = new Customer6();
        c.setFullName("Nguyễn Bá Duy");
        c.setDateOfBirth(LocalDate.of(2005, 1, 15));
        c.setAddress("Hà Đông, Hà Nội");
        c.setEmail(email);
        c.setPhoneNumber(phone);
        c.setUsername(username);
        c.setPassword("MatKhau@123");
        return c;
    }

    private int count(String sql) throws Exception {
        try (Statement s = con.createStatement(); ResultSet rs = s.executeQuery(sql)) { rs.next(); return rs.getInt(1); }
    }

    @Test void addCustomer_hopLe_luuDuHaiBang_vaBamMatKhau() throws Exception {
        int users = count("SELECT COUNT(*) FROM tblUser6"), customers = count("SELECT COUNT(*) FROM tblCustomer6");
        Customer6 c = newCustomer("duynb6", "duy@example.com", "0987654321");
        assertTrue(dao.addCustomer(c));
        assertTrue(c.getId() > 0);
        assertEquals(String.format("KH%06d", c.getId()), c.getCustomerCode());
        assertEquals(LocalDate.now(), c.getRegisteredDate());
        assertEquals(users + 1, count("SELECT COUNT(*) FROM tblUser6"));
        assertEquals(customers + 1, count("SELECT COUNT(*) FROM tblCustomer6"));
        assertEquals(0, count("SELECT COUNT(*) FROM tblUser6 WHERE password = 'MatKhau@123'"), "Mật khẩu phải được băm");
        assertNotNull(new UserDAO6(con).checkLogin("duynb6", "MatKhau@123"));
    }

    @Test void addCustomer_trungUsername_khongLuuGiDangDo() throws Exception {
        int users = count("SELECT COUNT(*) FROM tblUser6"), customers = count("SELECT COUNT(*) FROM tblCustomer6");
        assertFalse(dao.addCustomer(newCustomer("manager6", "khac@example.com", "0987000000")));
        assertEquals(users, count("SELECT COUNT(*) FROM tblUser6"));
        assertEquals(customers, count("SELECT COUNT(*) FROM tblCustomer6"));
    }

    @Test void addCustomer_trungEmail_traVeFalse() {
        assertFalse(dao.addCustomer(newCustomer("moi6", "an.nguyen@example.com", "0987000001")));
    }

    @Test void addCustomer_trungSoDienThoai_traVeFalse() {
        assertFalse(dao.addCustomer(newCustomer("moi7", "moi7@example.com", "0912345678")));
    }
}
