package com.example.supermarket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

/** Persists account credentials; the caller must pass an already-hashed password. */
public final class AccountDAO6 {
    private final Connection connection;

    public AccountDAO6(Connection connection) { this.connection = connection; }

    public boolean existsByUsername(String username) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT 1 FROM tblAccount6 WHERE LOWER(username)=?")) {
            statement.setString(1, username);
            try (ResultSet rows = statement.executeQuery()) { return rows.next(); }
        }
    }

    public void insert(long userId, String username, String passwordHash, LocalDateTime createdDate)
            throws SQLException {
        String sql = "INSERT INTO tblAccount6(userId,username,passwordHash,role,status,createdDate) VALUES (?,?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setString(2, username);
            statement.setString(3, passwordHash);
            statement.setString(4, "CUSTOMER");
            statement.setString(5, "ACTIVE");
            statement.setTimestamp(6, Timestamp.valueOf(createdDate));
            statement.executeUpdate();
        }
    }
}
