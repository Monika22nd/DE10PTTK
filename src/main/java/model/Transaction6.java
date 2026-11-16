package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Giao dịch (lớp trừu tượng): phiếu nhập, đơn trực tuyến hoặc hóa đơn bán tại quầy. Chứa các dòng TransactionDetail6. */
public abstract class Transaction6 implements java.io.Serializable {
    private int id;
    private String transactionCode;
    private LocalDateTime transactionDate;
    private String status;
    private String note;
    private List<TransactionDetail6> details = new ArrayList<>();

    public Transaction6() { super(); }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTransactionCode() { return transactionCode; }
    public void setTransactionCode(String transactionCode) { this.transactionCode = transactionCode; }

    public LocalDateTime getTransactionDate() { return transactionDate; }
    public void setTransactionDate(LocalDateTime transactionDate) { this.transactionDate = transactionDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }

    public List<TransactionDetail6> getDetails() { return details; }
    public void setDetails(List<TransactionDetail6> details) { this.details = details; }

    /** Tổng tiền = tổng thành tiền các dòng (dẫn xuất, không lưu CSDL). */
    public BigDecimal getTotalAmount() {
        BigDecimal total = BigDecimal.ZERO;
        for (TransactionDetail6 d : details) total = total.add(d.getAmount());
        return total;
    }

    /** Ngày giao dịch dạng dd/MM/yyyy HH:mm để hiển thị trên giao diện. */
    public String getTransactionDateText() {
        return transactionDate == null ? "" : transactionDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    /** Tên loại giao dịch để hiển thị. */
    public abstract String getTypeName();
}
