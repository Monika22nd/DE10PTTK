package dao;

import java.sql.Connection;

/**
 * Lớp DAO gốc: giữ kết nối CSDL dùng chung cho các lớp DAO con
 * (giáo trình mục 4.3.1 - bước 3: DAO kế thừa từ lớp trừu tượng để dùng chung kết nối).
 */
public abstract class DAO6 {
    protected Connection con;

    public DAO6(Connection con) {
        this.con = con;
    }
}
