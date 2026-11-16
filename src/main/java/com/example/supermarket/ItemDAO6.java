package com.example.supermarket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/** Retrieves an active item with its revenue derived from the same date filter. */
public final class ItemDAO6 {
    private final Database database;

    public ItemDAO6(Database database) { this.database = database; }

    public Map<String, Object> findById(long itemId, ItemStatisticsService6.Range range) throws SQLException {
        String sql = """
                SELECT i.id,i.itemCode,i.itemName,i.unit,i.sellingPrice,i.description,k.categoryName,
                       COALESCE(x.totalQuantitySold,0) AS totalQuantitySold,
                       COALESCE(x.totalRevenue,0) AS totalRevenue
                FROM tblItem6 i JOIN tblItemCategory6 k ON k.id=i.categoryId
                LEFT JOIN (
                  SELECT d.itemId,SUM(d.quantity) AS totalQuantitySold,SUM(d.quantity*d.unitPrice) AS totalRevenue
                  FROM tblTransactionDetail6 d JOIN tblTransaction6 t ON t.id=d.transactionId
                  WHERE t.status='COMPLETED' AND t.transactionType IN ('COUNTER_SALE','ONLINE_ORDER')
                    AND t.transactionDate>=? AND t.transactionDate<?
                  GROUP BY d.itemId
                ) x ON x.itemId=i.id
                WHERE i.id=? AND i.status='ACTIVE'
                """;
        try (Connection connection = database.open(); PreparedStatement statement = connection.prepareStatement(sql)) {
            TransactionDetailDAO6.setRange(statement, range, 1);
            statement.setLong(3, itemId);
            try (ResultSet result = statement.executeQuery()) {
                if (!result.next()) return null;
                Map<String, Object> item = new LinkedHashMap<>();
                for (String key : List.of("itemCode", "itemName", "unit", "description", "categoryName"))
                    item.put(key, result.getString(key));
                item.put("sellingPrice", result.getBigDecimal("sellingPrice"));
                item.put("totalQuantitySold", result.getInt("totalQuantitySold"));
                item.put("totalRevenue", result.getBigDecimal("totalRevenue"));
                return item;
            }
        }
    }
}
