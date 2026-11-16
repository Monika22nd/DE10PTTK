package com.example.supermarket;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

/** Persists the member profile linked to a User6 row. */
public final class CustomerDAO6 {
    private final Connection connection;

    public CustomerDAO6(Connection connection) { this.connection = connection; }

    public void insert(long userId, String customerCode, LocalDate registeredDate) throws SQLException {
        String sql = "INSERT INTO tblCustomer6(userId,customerCode,memberStatus,registeredDate) VALUES (?,?,?,?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            statement.setString(2, customerCode);
            statement.setString(3, "ACTIVE");
            statement.setDate(4, Date.valueOf(registeredDate));
            statement.executeUpdate();
        }
    }
}
