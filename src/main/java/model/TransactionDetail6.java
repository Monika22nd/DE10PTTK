package model;

import java.math.BigDecimal;

/** Dòng chi tiết của một giao dịch; unitPrice là đơn giá tại thời điểm giao dịch. */
public class TransactionDetail6 implements java.io.Serializable {
    private int id;
    private int quantity;
    private BigDecimal unitPrice;
    private Item6 item;

    public TransactionDetail6() { super(); }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public Item6 getItem() { return item; }
    public void setItem(Item6 item) { this.item = item; }

    /** Thành tiền của dòng = quantity x unitPrice (thuộc tính dẫn xuất, không lưu CSDL). */
    public BigDecimal getAmount() {
        return unitPrice == null ? BigDecimal.ZERO : unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
