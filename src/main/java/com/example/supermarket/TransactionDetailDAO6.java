package com.example.supermarket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Read-only queries over completed sale lines, matching the report design. */
public final class TransactionDetailDAO6 {
    private final Database database;

    public TransactionDetailDAO6(Database database) { this.database = database; }

    public List<Map<String, Object>> aggregateItemRevenue(ItemStatisticsService6.Range range) throws SQLException {
        String sql = """
                SELECT i.id,i.itemCode,i.itemName,SUM(d.quantity) AS totalQuantitySold,
                       SUM(d.quantity * d.unitPrice) AS totalRevenue
                FROM tblTransaction6 t JOIN tblTransactionDetail6 d ON d.transactionId=t.id
                JOIN tblItem6 i ON i.id=d.itemId
                WHERE t.status='COMPLETED' AND t.transactionType IN ('COUNTER_SALE','ONLINE_ORDER')
                  AND t.transactionDate>=? AND t.transactionDate<?
                GROUP BY i.id,i.itemCode,i.itemName
                ORDER BY totalRevenue DESC,i.itemCode ASC
                """;
        List<Map<String, Object>> rows = new ArrayList<>();
        try (Connection connection = database.open(); PreparedStatement statement = connection.prepareStatement(sql)) {
            setRange(statement, range, 1);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("id", result.getLong("id"));
                    row.put("itemCode", result.getString("itemCode"));
                    row.put("itemName", result.getString("itemName"));
                    row.put("totalQuantitySold", result.getInt("totalQuantitySold"));
                    row.put("totalRevenue", result.getBigDecimal("totalRevenue"));
                    rows.add(row);
                }
            }
        }
        return rows;
    }

    public List<Map<String, Object>> findSaleLines(long itemId, ItemStatisticsService6.Range range)
            throws SQLException {
        String sql = """
                SELECT t.transactionCode,t.transactionDate,t.transactionType,d.quantity,d.unitPrice,
                       d.quantity*d.unitPrice AS amount
                FROM tblTransaction6 t JOIN tblTransactionDetail6 d ON d.transactionId=t.id
                WHERE d.itemId=? AND t.status='COMPLETED'
                  AND t.transactionType IN ('COUNTER_SALE','ONLINE_ORDER')
                  AND t.transactionDate>=? AND t.transactionDate<?
                ORDER BY t.transactionDate DESC,t.transactionCode ASC
                """;
        List<Map<String, Object>> rows = new ArrayList<>();
        try (Connection connection = database.open(); PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, itemId);
            setRange(statement, range, 2);
            try (ResultSet result = statement.executeQuery()) {
                while (result.next()) {
                    Map<String, Object> row = new LinkedHashMap<>();
                    row.put("transactionCode", result.getString("transactionCode"));
                    row.put("transactionDate", result.getTimestamp("transactionDate")
                            .toLocalDateTime().toString().replace('T', ' '));
                    row.put("transactionType", result.getString("transactionType"));
                    row.put("quantity", result.getInt("quantity"));
                    row.put("unitPrice", result.getBigDecimal("unitPrice"));
                    row.put("amount", result.getBigDecimal("amount"));
                    rows.add(row);
                }
            }
        }
        return rows;
    }

    static void setRange(PreparedStatement statement, ItemStatisticsService6.Range range, int index)
            throws SQLException {
        statement.setTimestamp(index, Timestamp.valueOf(range.start().atStartOfDay()));
        statement.setTimestamp(index + 1, Timestamp.valueOf(range.end().plusDays(1).atStartOfDay()));
    }
}
