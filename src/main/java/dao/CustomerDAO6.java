package dao;

import model.Customer6;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/** Truy xuất bảng tblCustomer6 (và bảng cha tblUser6). */
public class CustomerDAO6 extends DAO6 {

    public CustomerDAO6(Connection con) {
        super(con);
    }

    /**
     * Lưu thành viên mới trong MỘT giao dịch CSDL:
     * 1) băm mật khẩu bằng BCrypt; 2) INSERT tblUser6, lấy id; 3) sinh customerCode = "KH" + id;
     * 4) INSERT tblCustomer6; 5) commit. Lỗi ở bất kỳ bước nào thì rollback, không để lại bản ghi dở dang.
     * Sau khi thành công, đối tượng c được gán id, customerCode, registeredDate, role = CUSTOMER.
     * @return true nếu lưu thành công, false nếu lỗi (ví dụ trùng username/email/số điện thoại).
     */
    public boolean addCustomer(Customer6 c) {
        boolean oldAutoCommit = true;
        try {
            oldAutoCommit = con.getAutoCommit();
            con.setAutoCommit(false);
            String sqlUser = "INSERT INTO tblUser6(username,password,fullName,dateOfBirth,address,email,phoneNumber,role) VALUES (?,?,?,?,?,?,?,?)";
            int userId;
            try (PreparedStatement ps = con.prepareStatement(sqlUser, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, c.getUsername());
                ps.setString(2, BCrypt.hashpw(c.getPassword(), BCrypt.gensalt(10)));
                ps.setString(3, c.getFullName());
                ps.setDate(4, Date.valueOf(c.getDateOfBirth()));
                ps.setString(5, c.getAddress());
                ps.setString(6, c.getEmail());
                ps.setString(7, c.getPhoneNumber());
                ps.setString(8, "CUSTOMER");
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) throw new SQLException("Không sinh được id người dùng");
                    userId = keys.getInt(1);
                }
            }
            String code = String.format("KH%06d", userId);
            LocalDate today = LocalDate.now();
            try (PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO tblCustomer6(userId,customerCode,registeredDate) VALUES (?,?,?)")) {
                ps.setInt(1, userId);
                ps.setString(2, code);
                ps.setDate(3, Date.valueOf(today));
                ps.executeUpdate();
            }
            con.commit();
            c.setId(userId);
            c.setCustomerCode(code);
            c.setRegisteredDate(today);
            c.setRole("CUSTOMER");
            c.setPassword(null); // không giữ mật khẩu thô trong bộ nhớ sau khi lưu
            return true;
        } catch (SQLException e) {
            try { con.rollback(); } catch (SQLException ignored) { }
            return false;
        } finally {
            try { con.setAutoCommit(oldAutoCommit); } catch (SQLException ignored) { }
        }
    }
}
