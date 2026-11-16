package model;

import java.time.LocalDateTime;

/** Lần giao một đơn hàng trực tuyến cho nhân viên giao hàng. */
public class Delivery6 implements java.io.Serializable {
    private int id;
    private OnlineOrder6 onlineOrder;
    private DeliveryStaff6 deliveryStaff;
    private LocalDateTime deliveryDate;
    private String status;

    public Delivery6() { super(); }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public OnlineOrder6 getOnlineOrder() { return onlineOrder; }
    public void setOnlineOrder(OnlineOrder6 onlineOrder) { this.onlineOrder = onlineOrder; }

    public DeliveryStaff6 getDeliveryStaff() { return deliveryStaff; }
    public void setDeliveryStaff(DeliveryStaff6 deliveryStaff) { this.deliveryStaff = deliveryStaff; }

    public LocalDateTime getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(LocalDateTime deliveryDate) { this.deliveryDate = deliveryDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
