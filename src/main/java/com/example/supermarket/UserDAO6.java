package com.example.supermarket;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

/** Persistence operations for the shared User6 identity. */
public final class UserDAO6 {
    private final Connection connection;

    public UserDAO6(Connection connection) { this.connection = connection; }

    public boolean existsByEmail(String email) throws SQLException {
        return exists("SELECT 1 FROM tblUser6 WHERE LOWER(email)=?", email);
    }

    public boolean existsByPhone(String phone) throws SQLException {
        return exists("SELECT 1 FROM tblUser6 WHERE phoneNumber=?", phone);
    }

    public long insert(String fullName, LocalDate dateOfBirth, String address,
                       String phoneNumber, String email) throws SQLException {
        String sql = "INSERT INTO tblUser6(fullName,dateOfBirth,address,phoneNumber,email) VALUES (?,?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, fullName);
            statement.setDate(2, Date.valueOf(dateOfBirth));
            statement.setString(3, address);
            statement.setString(4, phoneNumber);
            statement.setString(5, email);
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) throw new SQLException("User ID was not generated");
                return keys.getLong(1);
            }
        }
    }

    private boolean exists(String sql, String value) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, value);
            try (ResultSet rows = statement.executeQuery()) { return rows.next(); }
        }
    }
}
