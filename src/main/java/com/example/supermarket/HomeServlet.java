package com.example.supermarket;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public final class HomeServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        WebSupport.page(request, response, "index.jsp");
    }
}
