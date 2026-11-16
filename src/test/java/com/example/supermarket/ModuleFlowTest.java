package com.example.supermarket;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class ModuleFlowTest {
    private Database newDatabase() throws Exception {
        Database db = new Database("jdbc:h2:mem:" + UUID.randomUUID().toString().replace("-", "") + ";DB_CLOSE_DELAY=-1");
        db.initialize(true);
        return db;
    }

    @Test void registrationIsAtomicAndRejectsDuplicateData() throws Exception {
        Database db = newDatabase();
        var registration = new RegistrationService6(db);
        var form = Map.of("fullName", "Nguyễn Văn A", "dateOfBirth", "2001-05-10", "address", "Hà Nội",
                "phoneNumber", "0912345678", "email", "test@example.com", "username", "member6",
                "password", "password123", "confirmPassword", "password123");
        var result = registration.register(form);
        assertTrue(result.success());
        assertTrue(result.customerCode().startsWith("CUS"));
        assertEquals("CUSTOMER", new AuthService6(db).login("member6", "password123").role());
        var duplicate = registration.register(form);
        assertFalse(duplicate.success());
        assertTrue(duplicate.errors().containsKey("email"));
        assertTrue(duplicate.errors().containsKey("phoneNumber"));
        assertTrue(duplicate.errors().containsKey("username"));
        try (var c = db.open(); var s = c.createStatement(); var rs = s.executeQuery("SELECT COUNT(*) FROM tblCustomer6")) {
            rs.next(); assertEquals(1, rs.getInt(1));
        }
    }

    @Test void revenueUsesOnlyCompletedSalesAndShowsRelatedTransactions() throws Exception {
        Database db = newDatabase();
        var service = new ItemStatisticsService6(db);
        var range = new ItemStatisticsService6.Range(LocalDate.now().minusDays(30), LocalDate.now());
        var rows = service.list(range);
        assertEquals(2, rows.size());
        assertEquals("ITEM001", rows.get(0).get("itemCode"));
        assertEquals(3, rows.get(0).get("totalQuantitySold"));
        assertEquals("360000.00", rows.get(0).get("totalRevenue").toString());
        assertEquals(3, rows.get(1).get("totalQuantitySold")); // Pending quantity 100 excluded.
        long itemId = (Long) rows.get(0).get("id");
        assertEquals(2, service.transactions(itemId, range).size());
        assertEquals("360000.00", service.detail(itemId, range).get("totalRevenue").toString());
    }

    @Test void invalidDateRangeIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> ItemStatisticsService6.parseRange("2026-11-10", "2026-11-01"));
        assertThrows(IllegalArgumentException.class, () -> ItemStatisticsService6.parseRange("", "2026-11-01"));
    }
}
