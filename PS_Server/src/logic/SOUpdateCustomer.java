package logic;

import domain.Klijent;
import validator.impl.ValidatorCustomer;

public class SOUpdateCustomer extends SystemOperation {

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
