package dao;

import model.Customer6;
import model.ManagementStaff6;
import model.User6;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

/** Kiểm thử đơn vị UserDAO6 (giáo trình mục 5.3). */
class TestUserDAO6 {
    private Connection con;
    private UserDAO6 dao;

    @BeforeEach void setUp() throws Exception { con = TestSupport6.newDatabase(); dao = new UserDAO6(con); }
    @AfterEach void tearDown() throws Exception { con.close(); }

    @Test void checkUsername_daTonTai_khongPhanBietHoaThuong() throws Exception {
        assertTrue(dao.checkUsername("manager6"));
        assertTrue(dao.checkUsername("MANAGER6"));
    }

    @Test void checkUsername_chuaTonTai() throws Exception { assertFalse(dao.checkUsername("thanhvienmoi6")); }

    @Test void checkEmail_daTonTai_vaChuaTonTai() throws Exception {
        assertTrue(dao.checkEmail("an.nguyen@example.com"));
        assertFalse(dao.checkEmail("moi@example.com"));
    }

    @Test void checkPhoneNumber_daTonTai_vaChuaTonTai() throws Exception {
        assertTrue(dao.checkPhoneNumber("0912345678"));
        assertFalse(dao.checkPhoneNumber("0999999999"));
    }

    @Test void checkLogin_dungMatKhau_traVeDungLopCon() throws Exception {
        User6 u = dao.checkLogin("manager6", "Manager@123");
        assertNotNull(u);
        assertTrue(u instanceof ManagementStaff6);
        assertEquals("Trần Văn Quản", u.getFullName());
        assertTrue(dao.checkLogin("khachhang6", "Customer@123") instanceof Customer6);
    }

    @Test void checkLogin_saiMatKhau_hoacSaiTen() throws Exception {
        assertNull(dao.checkLogin("manager6", "sai-mat-khau"));
        assertNull(dao.checkLogin("khongtontai", "Manager@123"));
        assertNull(dao.checkLogin("manager6", null));
    }
}
