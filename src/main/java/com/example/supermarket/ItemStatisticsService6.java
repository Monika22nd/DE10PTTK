package com.example.supermarket;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/** Application logic for the read-only item-revenue report. */
public final class ItemStatisticsService6 {
    private final TransactionDetailDAO6 saleLines;
    private final ItemDAO6 items;

    public ItemStatisticsService6(Database database) {
        saleLines = new TransactionDetailDAO6(database);
        items = new ItemDAO6(database);
    }

    public record Range(LocalDate start, LocalDate end) {}

    public static Range parseRange(String start, String end) {
        if (start == null || start.isBlank() || end == null || end.isBlank())
            throw new IllegalArgumentException("Vui lòng chọn đủ ngày bắt đầu và kết thúc.");
        try {
            LocalDate first = LocalDate.parse(start);
            LocalDate last = LocalDate.parse(end);
            if (first.isAfter(last))
                throw new IllegalArgumentException("Ngày bắt đầu không được sau ngày kết thúc.");
            return new Range(first, last);
        } catch (java.time.format.DateTimeParseException e) {
            throw new IllegalArgumentException("Ngày không hợp lệ.");
        }
    }

    public List<Map<String, Object>> list(Range range) throws SQLException {
        return saleLines.aggregateItemRevenue(range);
    }

    public Map<String, Object> detail(long itemId, Range range) throws SQLException {
        return items.findById(itemId, range);
    }

    public List<Map<String, Object>> transactions(long itemId, Range range) throws SQLException {
        return saleLines.findSaleLines(itemId, range);
    }
}
