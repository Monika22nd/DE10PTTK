package model;

/** Nhân viên siêu thị (lớp trừu tượng). */
public abstract class Employee6 extends User6 {
    private String employeeCode;
    private String position;

    public Employee6() { super(); }

    public String getEmployeeCode() { return employeeCode; }
    public void setEmployeeCode(String employeeCode) { this.employeeCode = employeeCode; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
}
