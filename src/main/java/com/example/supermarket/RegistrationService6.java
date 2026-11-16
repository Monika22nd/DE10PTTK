package com.example.supermarket;

import org.mindrot.jbcrypt.BCrypt;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/** Validate -> check duplicates -> insert User6, Customer6 and Account6 in one transaction. */
public final class RegistrationService6 {
    private final Database database;
    public RegistrationService6(Database database) { this.database = database; }

    public record Result(String customerCode, Map<String, String> errors) {
        public boolean success() { return errors.isEmpty(); }
    }

    public Result register(Map<String, String> input) throws SQLException {
        Map<String, String> errors = validate(input);
        if (!errors.isEmpty()) return new Result(null, errors);

        String email = input.get("email").trim().toLowerCase();
        String username = input.get("username").trim().toLowerCase();
        String phone = input.get("phoneNumber").trim();
        try (UnitOfWork6 work = new UnitOfWork6(database)) {
            UserDAO6 users = new UserDAO6(work.connection());
            CustomerDAO6 customers = new CustomerDAO6(work.connection());
            AccountDAO6 accounts = new AccountDAO6(work.connection());

            if (users.existsByEmail(email)) errors.put("email", "Email đã được sử dụng.");
            if (users.existsByPhone(phone)) errors.put("phoneNumber", "Số điện thoại đã được sử dụng.");
            if (accounts.existsByUsername(username)) errors.put("username", "Tên đăng nhập đã tồn tại.");
            if (!errors.isEmpty()) return new Result(null, errors); // close() rolls back.

            long userId = users.insert(input.get("fullName").trim(),
                    LocalDate.parse(input.get("dateOfBirth")), input.get("address").trim(), phone, email);
            String code = "CUS" + String.format("%06d", userId);
            customers.insert(userId, code, LocalDate.now());
            accounts.insert(userId, username,
                    BCrypt.hashpw(input.get("password"), BCrypt.gensalt(12)), LocalDateTime.now());
            work.commit();
            return new Result(code, Map.of());
        } catch (SQLException e) {
            if ("23505".equals(e.getSQLState()))
                return new Result(null, Map.of("form", "Thông tin đã được đăng ký; vui lòng kiểm tra lại."));
            throw e;
        }
    }

    private static Map<String, String> validate(Map<String, String> in) {
        Map<String, String> errors = new LinkedHashMap<>();
        for (String key : new String[]{"fullName", "dateOfBirth", "address", "phoneNumber", "email", "username", "password", "confirmPassword"}) {
            if (in.getOrDefault(key, "").isBlank()) errors.put(key, "Không được để trống.");
        }
        if (!errors.containsKey("fullName") && in.get("fullName").trim().length() > 120) errors.put("fullName", "Tên quá dài.");
        if (!errors.containsKey("address") && in.get("address").trim().length() > 255) errors.put("address", "Địa chỉ quá dài.");
        if (!errors.containsKey("dateOfBirth")) {
            try {
                LocalDate dob = LocalDate.parse(in.get("dateOfBirth"));
                if (!dob.isBefore(LocalDate.now())) errors.put("dateOfBirth", "Ngày sinh phải trước hôm nay.");
            } catch (Exception e) { errors.put("dateOfBirth", "Ngày sinh không hợp lệ."); }
        }
        if (!errors.containsKey("phoneNumber") && !in.get("phoneNumber").trim().matches("[0-9]{9,11}")) errors.put("phoneNumber", "Số điện thoại phải có 9–11 chữ số.");
        if (!errors.containsKey("email") && !in.get("email").trim().matches("(?i)^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$")) errors.put("email", "Email không đúng định dạng.");
        if (!errors.containsKey("username") && !in.get("username").trim().matches("[A-Za-z0-9_]{4,30}")) errors.put("username", "Tên đăng nhập 4–30 ký tự, chỉ chữ, số hoặc _.");
        if (!errors.containsKey("password") && in.get("password").length() < 8) errors.put("password", "Mật khẩu ít nhất 8 ký tự.");
        if (!errors.containsKey("password") && in.get("password").length() > 72) errors.put("password", "Mật khẩu tối đa 72 ký tự.");
        if (!errors.containsKey("confirmPassword") && !java.util.Objects.equals(in.get("password"), in.get("confirmPassword"))) errors.put("confirmPassword", "Mật khẩu nhập lại không khớp.");
        return errors;
    }

}
