package model;

import java.math.BigDecimal;

/** Thống kê mặt hàng theo doanh thu: kế thừa Item6 (theo giáo trình, lớp thống kê kế thừa thực thể tương ứng). */
public class ItemStatistics6 extends Item6 {
    private int totalQuantity;
    private BigDecimal totalRevenue;

    public ItemStatistics6() { super(); }

    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }
}
