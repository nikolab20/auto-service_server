package logic;

import domain.DomainObject;
import domain.Klijent;

import java.util.List;

public class SOSearchCustomers extends SystemOperation {

    private String criteria;

    public SOSearchCustomers(String criteria) {
        super();
        this.criteria = criteria;
        //validator
    }

    @Override
    protected void operation() throws Exception {
        listOdo = dbbr.searchCustomer(criteria);
    }
}
