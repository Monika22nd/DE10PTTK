package com.example.supermarket;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

public final class RegisterServlet extends HttpServlet {
    private static final String[] FIELDS = {"fullName", "dateOfBirth", "address", "phoneNumber", "email", "username", "password", "confirmPassword"};

    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("form", Map.of());
        request.setAttribute("errors", Map.of());
        WebSupport.page(request, response, "register.jsp");
    }

    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        if (!WebSupport.validCsrf(request)) { response.sendError(403, "Biểu mẫu hết hạn; tải lại trang và thử lại."); return; }
        Map<String, String> input = new HashMap<>();
        for (String key : FIELDS) input.put(key, Objects.toString(request.getParameter(key), ""));
        try {
            RegistrationService6.Result result = WebSupport.services(request).registration().register(input);
            if (result.success()) {
                request.getSession().setAttribute("registeredCode", result.customerCode());
                response.sendRedirect(request.getContextPath() + "/register/success");
                return;
            }
            request.setAttribute("errors", result.errors());
        } catch (SQLException e) {
            request.setAttribute("errors", Map.of("form", "Không thể lưu đăng ký. Vui lòng thử lại."));
        }
        input.remove("password"); input.remove("confirmPassword");
        request.setAttribute("form", input);
        WebSupport.page(request, response, "register.jsp");
    }
}
