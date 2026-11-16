package model;

import java.time.LocalDate;

/** Thành viên của hệ thống (lớp trừu tượng). Thông tin đăng nhập nằm ngay trong User6 như lớp Thanhvien của giáo trình. */
public abstract class User6 implements java.io.Serializable {
    private int id;
    private String username;
    private String password;
    private String fullName;
    private LocalDate dateOfBirth;
    private String address;
    private String email;
    private String phoneNumber;
    private String role;

    public User6() { super(); }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
