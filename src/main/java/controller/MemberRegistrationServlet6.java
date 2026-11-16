package controller;

import dao.CustomerDAO6;
import dao.Database6;
import dao.UserDAO6;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Customer6;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Controller module Register as Member 6.
 * GET /register: hiển thị gdMemberRegistration6.jsp; POST /register: kiểm tra và lưu;
 * GET /register/success: hiển thị gdRegistrationSuccess6.jsp.
 */
public class MemberRegistrationServlet6 extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if ("/register/success".equals(req.getServletPath())) {
            Customer6 c = (Customer6) req.getSession().getAttribute("registeredCustomer");
            if (c == null) {
                resp.sendRedirect(req.getContextPath() + "/register");
                return;
            }
            req.getSession().removeAttribute("registeredCustomer");
            req.setAttribute("customer", c);
            WebUtil6.forward(req, resp, "gdRegistrationSuccess6.jsp");
            return;
        }
        showForm(req, resp, new Customer6(), new LinkedHashMap<>());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        if (!WebUtil6.validCsrf(req)) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Biểu mẫu đã hết hạn, vui lòng tải lại trang.");
            return;
        }
        Customer6 c = new Customer6();
        c.setFullName(WebUtil6.param(req, "fullName"));
        c.setAddress(WebUtil6.param(req, "address"));
        c.setPhoneNumber(WebUtil6.param(req, "phoneNumber"));
        c.setEmail(WebUtil6.param(req, "email").toLowerCase());
        c.setUsername(WebUtil6.param(req, "username"));
        String password = req.getParameter("password") == null ? "" : req.getParameter("password");
        String confirm = req.getParameter("confirmPassword") == null ? "" : req.getParameter("confirmPassword");
        String dob = WebUtil6.param(req, "dateOfBirth");

        Map<String, String> errors = validate(c, dob, password, confirm);
        if (errors.isEmpty()) {
            try (Connection con = Database6.getConnection()) {
                UserDAO6 userDAO = new UserDAO6(con);
                if (userDAO.checkUsername(c.getUsername())) errors.put("username", "Tên đăng nhập đã tồn tại.");
                if (userDAO.checkEmail(c.getEmail())) errors.put("email", "Email đã được đăng ký.");
                if (userDAO.checkPhoneNumber(c.getPhoneNumber())) errors.put("phoneNumber", "Số điện thoại đã được đăng ký.");
                if (errors.isEmpty()) {
                    c.setPassword(password);
                    if (new CustomerDAO6(con).addCustomer(c)) {
                        req.getSession().setAttribute("registeredCustomer", c);
                        resp.sendRedirect(req.getContextPath() + "/register/success");
                        return;
                    }
                    errors.put("form", "Đăng ký thất bại, dữ liệu chưa được lưu. Vui lòng thử lại.");
                }
            } catch (SQLException e) {
                errors.put("form", "Không kết nối được cơ sở dữ liệu. Vui lòng thử lại.");
            }
        }
        req.setAttribute("dateOfBirthText", dob);
        showForm(req, resp, c, errors);
    }

    /** Kiểm tra trường bắt buộc, định dạng và mật khẩu. Trả về Map tên trường -> thông báo lỗi. */
    Map<String, String> validate(Customer6 c, String dob, String password, String confirm) {
        Map<String, String> e = new LinkedHashMap<>();
        if (c.getFullName().isEmpty()) e.put("fullName", "Vui lòng nhập họ tên.");
        else if (c.getFullName().length() > 120) e.put("fullName", "Họ tên tối đa 120 ký tự.");
        if (dob.isEmpty()) e.put("dateOfBirth", "Vui lòng chọn ngày sinh.");
        else {
            try {
                LocalDate d = LocalDate.parse(dob);
                if (d.isAfter(LocalDate.now())) e.put("dateOfBirth", "Ngày sinh không được lớn hơn ngày hiện tại.");
                else c.setDateOfBirth(d);
            } catch (DateTimeParseException ex) {
                e.put("dateOfBirth", "Ngày sinh không hợp lệ.");
            }
        }
        if (c.getAddress().isEmpty()) e.put("address", "Vui lòng nhập địa chỉ.");
        else if (c.getAddress().length() > 255) e.put("address", "Địa chỉ tối đa 255 ký tự.");
        if (!c.getPhoneNumber().matches("0[0-9]{9,10}")) e.put("phoneNumber", "Số điện thoại gồm 10–11 chữ số, bắt đầu bằng 0.");
        if (!c.getEmail().matches("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,}$")) e.put("email", "Email không đúng định dạng.");
        if (!c.getUsername().matches("[A-Za-z0-9_]{4,30}")) e.put("username", "Tên đăng nhập 4–30 ký tự, chỉ gồm chữ, số hoặc dấu _.");
        if (password.length() < 8 || password.length() > 72) e.put("password", "Mật khẩu dài 8–72 ký tự.");
        else if (!password.equals(confirm)) e.put("confirmPassword", "Mật khẩu nhập lại không khớp.");
        return e;
    }

    private void showForm(HttpServletRequest req, HttpServletResponse resp, Customer6 c, Map<String, String> errors)
            throws ServletException, IOException {
        req.setAttribute("customer", c);
        req.setAttribute("errors", errors);
        WebUtil6.forward(req, resp, "gdMemberRegistration6.jsp");
    }
}
