package dao;

import model.Customer6;
import model.DeliveryStaff6;
import model.ManagementStaff6;
import model.Saler6;
import model.User6;
import model.WarehouseStaff6;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** Truy xuất bảng tblUser6: kiểm tra trùng thông tin khi đăng ký và kiểm tra đăng nhập. */
public class UserDAO6 extends DAO6 {

    public UserDAO6(Connection con) {
        super(con);
    }

    /** @return true nếu tên đăng nhập đã tồn tại (không phân biệt hoa thường). */
    public boolean checkUsername(String username) throws SQLException {
        return exists("SELECT 1 FROM tblUser6 WHERE LOWER(username) = LOWER(?)", username);
    }

    /** @return true nếu email đã được dùng bởi một người dùng khác. */
    public boolean checkEmail(String email) throws SQLException {
        return exists("SELECT 1 FROM tblUser6 WHERE LOWER(email) = LOWER(?)", email);
    }

    /** @return true nếu số điện thoại đã được dùng bởi một người dùng khác. */
    public boolean checkPhoneNumber(String phoneNumber) throws SQLException {
        return exists("SELECT 1 FROM tblUser6 WHERE phoneNumber = ?", phoneNumber);
    }

    /**
     * Kiểm tra đăng nhập.
     * @return đối tượng User6 đúng lớp con theo vai trò (Customer6, ManagementStaff6...) hoặc null nếu sai.
     */
    public User6 checkLogin(String username, String password) throws SQLException {
        String sql = "SELECT * FROM tblUser6 WHERE LOWER(username) = LOWER(?)";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                String hash = rs.getString("password");
                if (password == null || !BCrypt.checkpw(password, hash)) return null;
                User6 user = newUserOfRole(rs.getString("role"));
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setFullName(rs.getString("fullName"));
                user.setEmail(rs.getString("email"));
                user.setPhoneNumber(rs.getString("phoneNumber"));
                user.setAddress(rs.getString("address"));
                user.setRole(rs.getString("role"));
                if (rs.getDate("dateOfBirth") != null) user.setDateOfBirth(rs.getDate("dateOfBirth").toLocalDate());
                return user;
            }
        }
    }

    private static User6 newUserOfRole(String role) {
        switch (role) {
            case "MANAGEMENT_STAFF": return new ManagementStaff6();
            case "WAREHOUSE_STAFF": return new WarehouseStaff6();
            case "SALER": return new Saler6();
            case "DELIVERY_STAFF": return new DeliveryStaff6();
            default: return new Customer6();
        }
    }

    private boolean exists(String sql, String value) throws SQLException {
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, value);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }
}
