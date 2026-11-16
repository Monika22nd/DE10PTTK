package com.example.supermarket;

import jakarta.servlet.*;

public final class AppListener implements ServletContextListener {
    @Override public void contextInitialized(ServletContextEvent event) {
        try {
            Database database = Database.local();
            database.initialize(Boolean.parseBoolean(System.getProperty("supermarket.demo", "false")));
            event.getServletContext().setAttribute("services", new AppServices(
                    new RegistrationService6(database), new AuthService6(database), new ItemStatisticsService6(database)));
        } catch (Exception e) { throw new IllegalStateException("Không thể khởi tạo cơ sở dữ liệu", e); }
    }
}
