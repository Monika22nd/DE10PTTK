package model;

/** Phiếu nhập hàng từ nhà cung cấp (không tính vào doanh thu). */
public class GoodsReceipt6 extends Transaction6 {
    private Supplier6 supplier;
    private WarehouseStaff6 warehouseStaff;

    public GoodsReceipt6() { super(); }

    public Supplier6 getSupplier() { return supplier; }
    public void setSupplier(Supplier6 supplier) { this.supplier = supplier; }

    public WarehouseStaff6 getWarehouseStaff() { return warehouseStaff; }
    public void setWarehouseStaff(WarehouseStaff6 warehouseStaff) { this.warehouseStaff = warehouseStaff; }

    @Override public String getTypeName() { return "Phiếu nhập hàng"; }
}
