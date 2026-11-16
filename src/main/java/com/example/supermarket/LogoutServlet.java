package com.example.supermarket;

import jakarta.servlet.http.*;
import java.io.IOException;

public final class LogoutServlet extends HttpServlet {
    @Override protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (!WebSupport.validCsrf(request)) { response.sendError(403); return; }
        request.getSession().invalidate();
        response.sendRedirect(request.getContextPath() + "/");
    }
}
