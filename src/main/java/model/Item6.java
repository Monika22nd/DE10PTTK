package model;

import java.math.BigDecimal;

/** Mặt hàng. Item6 chứa thuộc tính category kiểu ItemCategory6. */
public class Item6 implements java.io.Serializable {
    private int id;
    private String itemCode;
    private String name;
    private String unit;
    private BigDecimal sellingPrice;
    private int quantityOnHand;
    private String status;
    private String description;
    private ItemCategory6 category;

    public Item6() { super(); }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getItemCode() { return itemCode; }
    public void setItemCode(String itemCode) { this.itemCode = itemCode; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public BigDecimal getSellingPrice() { return sellingPrice; }
    public void setSellingPrice(BigDecimal sellingPrice) { this.sellingPrice = sellingPrice; }

    public int getQuantityOnHand() { return quantityOnHand; }
    public void setQuantityOnHand(int quantityOnHand) { this.quantityOnHand = quantityOnHand; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public ItemCategory6 getCategory() { return category; }
    public void setCategory(ItemCategory6 category) { this.category = category; }
}
