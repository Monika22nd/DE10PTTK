package controller;

import dao.Database6;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;

/** Khởi tạo CSDL khi ứng dụng khởi động; nạp dữ liệu mẫu khi chạy demo (-Dsupermarket.demo=true). */
public class AppContextListener6 implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent event) {
        try {
            Database6.initialize(Boolean.parseBoolean(System.getProperty("supermarket.demo", "false")));
        } catch (Exception e) {
            throw new IllegalStateException("Không thể khởi tạo cơ sở dữ liệu", e);
        }
    }
}
