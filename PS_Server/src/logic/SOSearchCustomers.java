package logic;

public class SOSearchCustomers extends SystemOperation {

    /**
     * Criteria for customers search.
     */
    private final Long customerID;

    /**
     * Parameterized constructor for this system operation.
     *
     * @param customerID is criteria for search.
     */
    public SOSearchCustomers(Long customerID) {
        super();
        this.customerID = customerID;
    }

    @Override
    protected void operation() throws Exception {
        listOdo = dbbr.searchCustomer(customerID);
    }
}
