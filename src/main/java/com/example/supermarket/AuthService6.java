package com.example.supermarket;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.*;

public final class AuthService6 {
    private final Database database;
    public AuthService6(Database database) { this.database = database; }

    public record User(long id, String fullName, String role) {}

    public User login(String username, String password) throws SQLException {
        try (Connection c = database.open(); PreparedStatement s = c.prepareStatement(
                "SELECT u.id,u.fullName,a.passwordHash,a.role FROM tblAccount6 a JOIN tblUser6 u ON u.id=a.userId WHERE LOWER(a.username)=? AND a.status='ACTIVE'")) {
            s.setString(1, username.trim().toLowerCase());
            try (ResultSet rs = s.executeQuery()) {
                if (!rs.next() || !BCrypt.checkpw(password, rs.getString("passwordHash"))) return null;
                return new User(rs.getLong("id"), rs.getString("fullName"), rs.getString("role"));
            }
        }
    }
}
