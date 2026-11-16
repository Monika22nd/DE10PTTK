package model;

import java.time.LocalDate;

/** Khách hàng đã đăng ký thành viên. */
public class Customer6 extends User6 {
    private String customerCode;
    private LocalDate registeredDate;

    public Customer6() { super(); }

    public String getCustomerCode() { return customerCode; }
    public void setCustomerCode(String customerCode) { this.customerCode = customerCode; }

    public LocalDate getRegisteredDate() { return registeredDate; }
    public void setRegisteredDate(LocalDate registeredDate) { this.registeredDate = registeredDate; }
}
