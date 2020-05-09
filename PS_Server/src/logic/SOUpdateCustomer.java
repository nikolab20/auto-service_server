package logic;

import domain.Klijent;
import validator.impl.ValidatorCustomer;

public class SOUpdateCustomer extends SystemOperation {

    /**
     * Parameterized constructor for this system operation.
     * 
     * @param klijent is customer that user need to update.
     */
    public SOUpdateCustomer(Klijent klijent) {
        super();
        odo = klijent;
        validator = new ValidatorCustomer();
    }

    @Override
    protected void operation() throws Exception {
        dbbr.updateDomainObject(odo);
    }
}
