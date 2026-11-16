package com.example.supermarket;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

public final class RegisterSuccessServlet extends HttpServlet {
    @Override protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Object code = request.getSession().getAttribute("registeredCode");
        if (code == null) { response.sendRedirect(request.getContextPath() + "/register"); return; }
        request.getSession().removeAttribute("registeredCode");
        request.setAttribute("customerCode", code);
        WebSupport.page(request, response, "register-success.jsp");
    }
}
