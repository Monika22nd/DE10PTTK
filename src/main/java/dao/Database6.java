package dao;

import org.mindrot.jbcrypt.BCrypt;

import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Cấu hình kết nối CSDL (H2 khi chạy demo; có thể đổi URL sang MySQL),
 * tạo bảng từ schema.sql và nạp dữ liệu mẫu.
 */
public final class Database6 {
    private static String url = System.getProperty("supermarket.db.url",
            System.getenv().getOrDefault("SUPERMARKET_DB_URL", "jdbc:h2:file:./data/supermarket6v2;AUTO_SERVER=TRUE"));
    private static String user = "sa";
    private static String password = "";

    private Database6() {}

    public static void configure(String newUrl) { url = newUrl; }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    /** Tạo các bảng (nếu chưa có) và nạp dữ liệu mẫu khi demo = true. */
    public static void initialize(boolean demo) throws Exception {
        try (Connection con = getConnection(); Statement st = con.createStatement();
             InputStream in = Database6.class.getResourceAsStream("/schema.sql")) {
            if (in == null) throw new IllegalStateException("Thiếu schema.sql");
            String sql = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            StringBuilder clean = new StringBuilder();
            for (String line : sql.split("\n")) if (!line.trim().startsWith("--")) clean.append(line).append('\n');
            for (String command : clean.toString().split(";")) if (!command.isBlank()) st.execute(command.trim());
        }
        if (demo) seedDemoData();
    }

    /**
     * Dữ liệu mẫu: 1 quản lý, 1 bán hàng, 1 kho, 1 khách hàng; 4 mặt hàng (1 mặt hàng đã ngừng bán);
     * 1 phiếu nhập, 2 hóa đơn tại quầy, 2 đơn trực tuyến (1 đơn PENDING không được tính doanh thu).
     */
    public static void seedDemoData() throws SQLException {
        try (Connection c = getConnection()) {
            if (count(c, "SELECT COUNT(*) FROM tblUser6") > 0) return;
            c.setAutoCommit(false);
            try {
                int manager = user(c, "manager6", "Manager@123", "Trần Văn Quản", "1988-03-12", "Hà Nội", "manager6@sieuthi6.vn", "0901000006", "MANAGEMENT_STAFF");
                employee(c, manager, "NV001", "Trưởng phòng kinh doanh");
                int saler = user(c, "saler6", "Saler@123", "Lê Thị Bán", "1995-07-20", "Hà Nội", "saler6@sieuthi6.vn", "0901000007", "SALER");
                employee(c, saler, "NV002", "Thu ngân");
                int warehouse = user(c, "kho6", "Kho@12345", "Phạm Văn Kho", "1992-11-02", "Hà Nội", "kho6@sieuthi6.vn", "0901000008", "WAREHOUSE_STAFF");
                employee(c, warehouse, "NV003", "Thủ kho");
                int customer = user(c, "khachhang6", "Customer@123", "Nguyễn Văn An", "2001-05-10", "Cầu Giấy, Hà Nội", "an.nguyen@example.com", "0912345678", "CUSTOMER");
                exec(c, "INSERT INTO tblCustomer6(userId,customerCode,registeredDate) VALUES (?,?,?)", customer, "KH000004", Date.valueOf(LocalDate.now().minusDays(30)));

                int supplier = insert(c, "INSERT INTO tblSupplier6(supplierCode,name,address,email,phoneNumber,description) VALUES (?,?,?,?,?,?)",
                        "NCC01", "Công ty Lương thực Miền Bắc", "Long Biên, Hà Nội", "lienhe@luongthuc.vn", "02438123456", "Cung cấp gạo và đồ khô");
                int dry = insert(c, "INSERT INTO tblItemCategory6(name,description) VALUES (?,?)", "Thực phẩm khô", "Gạo, mì, dầu ăn");
                int dairy = insert(c, "INSERT INTO tblItemCategory6(name,description) VALUES (?,?)", "Sữa và chế phẩm", "Sữa tươi, sữa chua");
                int rice = item(c, "ITEM001", "Gạo thơm ST25 túi 5kg", "Túi", 180000, 50, "ACTIVE", "Gạo thơm đóng túi 5kg", dry);
                int milk = item(c, "ITEM002", "Sữa tươi tiệt trùng 1L", "Hộp", 35000, 120, "ACTIVE", "Sữa tươi nguyên chất", dairy);
                int noodle = item(c, "ITEM003", "Mì ăn liền thùng 30 gói", "Thùng", 120000, 0, "INACTIVE", "Đã ngừng kinh doanh", dry);
                item(c, "ITEM004", "Dầu ăn đậu nành 1L", "Chai", 55000, 40, "ACTIVE", "Chưa phát sinh bán", dry);

                LocalDate today = LocalDate.now();
                int gr = transaction(c, "PN001", today.minusDays(5).atTime(9, 0), "COMPLETED", "Nhập gạo đầu tháng");
                exec(c, "INSERT INTO tblGoodsReceipt6(transactionId,supplierId,warehouseStaffId) VALUES (?,?,?)", gr, supplier, warehouse);
                detail(c, gr, rice, 20, 150000);

                int hd1 = transaction(c, "HD001", today.minusDays(2).atTime(10, 30), "COMPLETED", null);
                exec(c, "INSERT INTO tblCounterSale6(transactionId,salerId,customerId) VALUES (?,?,?)", hd1, saler, null);
                detail(c, hd1, rice, 2, 180000);
                detail(c, hd1, milk, 3, 35000);

                int dh1 = transaction(c, "DH001", today.minusDays(1).atTime(14, 15), "COMPLETED", null);
                exec(c, "INSERT INTO tblOnlineOrder6(transactionId,customerId,approvedById,shippingAddress) VALUES (?,?,?,?)", dh1, customer, warehouse, "Cầu Giấy, Hà Nội");
                detail(c, dh1, rice, 1, 175000);
                detail(c, dh1, noodle, 2, 120000);

                int dh2 = transaction(c, "DH002", today.minusDays(1).atTime(15, 0), "PENDING", "Chưa duyệt - không tính doanh thu");
                exec(c, "INSERT INTO tblOnlineOrder6(transactionId,customerId,approvedById,shippingAddress) VALUES (?,?,?,?)", dh2, customer, null, "Cầu Giấy, Hà Nội");
                detail(c, dh2, milk, 100, 35000);

                int hd2 = transaction(c, "HD002", today.atTime(0, 5), "COMPLETED", null);
                exec(c, "INSERT INTO tblCounterSale6(transactionId,salerId,customerId) VALUES (?,?,?)", hd2, saler, customer);
                detail(c, hd2, milk, 4, 34000);
                c.commit();
            } catch (SQLException e) {
                c.rollback();
                throw e;
            } finally {
                c.setAutoCommit(true);
            }
        }
    }

    private static int user(Connection c, String username, String rawPassword, String fullName, String dob, String address,
                            String email, String phone, String role) throws SQLException {
        return insert(c, "INSERT INTO tblUser6(username,password,fullName,dateOfBirth,address,email,phoneNumber,role) VALUES (?,?,?,?,?,?,?,?)",
                username, BCrypt.hashpw(rawPassword, BCrypt.gensalt(10)), fullName, Date.valueOf(dob), address, email, phone, role);
    }

    private static void employee(Connection c, int userId, String code, String position) throws SQLException {
        exec(c, "INSERT INTO tblEmployee6(userId,employeeCode,position) VALUES (?,?,?)", userId, code, position);
    }

    private static int item(Connection c, String code, String name, String unit, long price, int qty, String status,
                            String description, int categoryId) throws SQLException {
        return insert(c, "INSERT INTO tblItem6(itemCode,name,unit,sellingPrice,quantityOnHand,status,description,categoryId) VALUES (?,?,?,?,?,?,?,?)",
                code, name, unit, BigDecimal.valueOf(price), qty, status, description, categoryId);
    }

    private static int transaction(Connection c, String code, LocalDateTime date, String status, String note) throws SQLException {
        return insert(c, "INSERT INTO tblTransaction6(transactionCode,transactionDate,status,note) VALUES (?,?,?,?)",
                code, Timestamp.valueOf(date), status, note);
    }

    private static void detail(Connection c, int transactionId, int itemId, int qty, long unitPrice) throws SQLException {
        exec(c, "INSERT INTO tblTransactionDetail6(quantity,unitPrice,transactionId,itemId) VALUES (?,?,?,?)",
                qty, BigDecimal.valueOf(unitPrice), transactionId, itemId);
    }

    private static int count(Connection c, String sql) throws SQLException {
        try (Statement s = c.createStatement(); ResultSet rs = s.executeQuery(sql)) {
            rs.next();
            return rs.getInt(1);
        }
    }

    private static void exec(Connection c, String sql, Object... args) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql)) {
            for (int i = 0; i < args.length; i++) ps.setObject(i + 1, args[i]);
            ps.executeUpdate();
        }
    }

    private static int insert(Connection c, String sql, Object... args) throws SQLException {
        try (PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            for (int i = 0; i < args.length; i++) ps.setObject(i + 1, args[i]);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                return keys.getInt(1);
            }
        }
    }
}
