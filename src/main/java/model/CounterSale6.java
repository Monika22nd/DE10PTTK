package model;

/** Hóa đơn bán hàng tại quầy. */
public class CounterSale6 extends Transaction6 {
    private Saler6 saler;
    private Customer6 customer;

    public CounterSale6() { super(); }

    public Saler6 getSaler() { return saler; }
    public void setSaler(Saler6 saler) { this.saler = saler; }

    public Customer6 getCustomer() { return customer; }
    public void setCustomer(Customer6 customer) { this.customer = customer; }

    @Override public String getTypeName() { return "Bán tại quầy"; }
}
