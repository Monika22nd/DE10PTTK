package dao;

import java.sql.Connection;
import java.util.UUID;

/** Tạo CSDL H2 trong bộ nhớ, mỗi lớp test một CSDL riêng có dữ liệu mẫu (Database6.seedDemoData). */
final class TestSupport6 {
    private TestSupport6() {}

    static Connection newDatabase() throws Exception {
        Database6.configure("jdbc:h2:mem:t" + UUID.randomUUID().toString().replace("-", "") + ";DB_CLOSE_DELAY=-1");
        Database6.initialize(true);
        return Database6.getConnection();
    }
}
