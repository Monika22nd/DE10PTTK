package com.example.supermarket;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public final class ReportsServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!WebSupport.manager(request, response)) return;
        WebSupport.page(request, response, "reports.jsp");
    }
}
