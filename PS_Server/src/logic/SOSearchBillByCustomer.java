/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

/**
 *
 * @author nikol
 */
public class SOSearchBillByCustomer extends SystemOperation {

    /**
     * Criteria for bills search.
     */
    private final Long customerId;

    /**
     * Parameterized constructor for this system operation.
     *
     * @param customerId is criteria for search.
     */
    public SOSearchBillByCustomer(Long customerId) {
        super();
        this.customerId = customerId;
    }

    @Override
    protected void operation() throws Exception {
        listOdo = dbbr.searchBillByCustomerId(customerId);
    }
}
