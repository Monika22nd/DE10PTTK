package com.example.supermarket;

import org.mindrot.jbcrypt.BCrypt;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** JDBC persistence for the two modules. H2 is used so the demo runs without MySQL setup. */
public final class Database {
    private final String url;

    public Database(String url) { this.url = url; }

    public static Database local() throws Exception {
        Files.createDirectories(Path.of("data"));
        return new Database(System.getenv().getOrDefault("SUPERMARKET_DB_URL", "jdbc:h2:file:./data/supermarket6;AUTO_SERVER=TRUE"));
    }

    public Connection open() throws SQLException { return DriverManager.getConnection(url, "sa", ""); }

    public void initialize(boolean sampleData) throws Exception {
        try (Connection connection = open(); Statement statement = connection.createStatement();
             InputStream input = Database.class.getResourceAsStream("/schema.sql")) {
            if (input == null) throw new IllegalStateException("schema.sql missing");
            String sql = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            for (String command : sql.split(";")) if (!command.isBlank()) statement.execute(command.trim());
        }
        if (sampleData) seedDemoData();
    }

    private void seedDemoData() throws SQLException {
        try (Connection c = open()) {
            c.setAutoCommit(false);
            try {
                if (count(c, "SELECT COUNT(*) FROM tblAccount6 WHERE username='manager6'") == 0) {
                    long id = insert(c, "INSERT INTO tblUser6(fullName,dateOfBirth,address,phoneNumber,email) VALUES (?,?,?,?,?)",
                            "Demo Manager", Date.valueOf("1990-01-01"), "Supermarket", "0900000006", "manager6@example.local");
                    insert(c, "INSERT INTO tblAccount6(userId,username,passwordHash,role,status,createdDate) VALUES (?,?,?,?,?,?)",
                            id, "manager6", BCrypt.hashpw("Manager@123", BCrypt.gensalt()), "MANAGEMENT_STAFF", "ACTIVE", Timestamp.valueOf(LocalDateTime.now()));
                    insert(c, "INSERT INTO tblEmployee6(userId,employeeCode,position,staffType) VALUES (?,?,?,?)",
                            id, "EMP0006", "Manager", "MANAGEMENT");
                }
                if (count(c, "SELECT COUNT(*) FROM tblItem6") == 0) {
                    long category = insert(c, "INSERT INTO tblItemCategory6(categoryCode,categoryName,description) VALUES (?,?,?)", "FOOD", "Thực phẩm", "Dữ liệu mẫu");
                    long rice = insert(c, "INSERT INTO tblItem6(categoryId,itemCode,itemName,unit,purchasePrice,sellingPrice,status,description) VALUES (?,?,?,?,?,?,?,?)",
                            category, "ITEM001", "Gạo thơm 5kg", "Túi", 90000, 120000, "ACTIVE", "Gạo thơm đóng túi");
                    long milk = insert(c, "INSERT INTO tblItem6(categoryId,itemCode,itemName,unit,purchasePrice,sellingPrice,status,description) VALUES (?,?,?,?,?,?,?,?)",
                            category, "ITEM002", "Sữa tươi 1L", "Hộp", 25000, 35000, "ACTIVE", "Sữa tươi nguyên chất");
                    LocalDate today = LocalDate.now();
                    long counter = insert(c, "INSERT INTO tblTransaction6(transactionCode,transactionDate,transactionType,status) VALUES (?,?,?,?)",
                            "SALE-DEMO-1", Timestamp.valueOf(today.minusDays(2).atTime(10, 30)), "COUNTER_SALE", "COMPLETED");
                    insert(c, "INSERT INTO tblTransactionDetail6(transactionId,itemId,quantity,unitPrice) VALUES (?,?,?,?)", counter, rice, 2, 120000);
                    insert(c, "INSERT INTO tblTransactionDetail6(transactionId,itemId,quantity,unitPrice) VALUES (?,?,?,?)", counter, milk, 3, 35000);
                    long online = insert(c, "INSERT INTO tblTransaction6(transactionCode,transactionDate,transactionType,status) VALUES (?,?,?,?)",
                            "SALE-DEMO-2", Timestamp.valueOf(today.minusDays(1).atTime(14, 15)), "ONLINE_ORDER", "COMPLETED");
                    insert(c, "INSERT INTO tblTransactionDetail6(transactionId,itemId,quantity,unitPrice) VALUES (?,?,?,?)", online, rice, 1, 120000);
                    long pending = insert(c, "INSERT INTO tblTransaction6(transactionCode,transactionDate,transactionType,status) VALUES (?,?,?,?)",
                            "SALE-DEMO-PENDING", Timestamp.valueOf(today.minusDays(1).atTime(15, 0)), "ONLINE_ORDER", "PENDING");
                    insert(c, "INSERT INTO tblTransactionDetail6(transactionId,itemId,quantity,unitPrice) VALUES (?,?,?,?)", pending, milk, 100, 35000);
                }
                c.commit();
            } catch (SQLException e) { c.rollback(); throw e; }
        }
    }

    private static int count(Connection c, String query) throws SQLException {
        try (Statement s = c.createStatement(); ResultSet rs = s.executeQuery(query)) { rs.next(); return rs.getInt(1); }
    }

    private static long insert(Connection c, String sql, Object... args) throws SQLException {
        try (PreparedStatement s = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < args.length; i++) s.setObject(i + 1, args[i]);
            s.executeUpdate();
            try (ResultSet keys = s.getGeneratedKeys()) { return keys.next() ? keys.getLong(1) : -1; }
        }
    }
}
