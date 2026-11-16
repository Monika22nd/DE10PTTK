package model;

/** Đơn hàng trực tuyến của khách hàng. */
public class OnlineOrder6 extends Transaction6 {
    private Customer6 customer;
    private WarehouseStaff6 approvedBy;
    private String shippingAddress;

    public OnlineOrder6() { super(); }

    public Customer6 getCustomer() { return customer; }
    public void setCustomer(Customer6 customer) { this.customer = customer; }

    public WarehouseStaff6 getApprovedBy() { return approvedBy; }
    public void setApprovedBy(WarehouseStaff6 approvedBy) { this.approvedBy = approvedBy; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    @Override public String getTypeName() { return "Đơn hàng trực tuyến"; }
}
