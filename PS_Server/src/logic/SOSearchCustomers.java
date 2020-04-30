package logic;

public class SOSearchCustomers extends SystemOperation {

    private final Long customerID;

    public SOSearchCustomers(Long customerID) {
        super();
        this.customerID = customerID;
    }

    @Override
    protected void operation() throws Exception {
        listOdo = dbbr.searchCustomer(customerID);
    }
}
