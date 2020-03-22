package logic;

import domain.Klijent;

public class SOUpdateCustomer extends SystemOperation {

    public SOUpdateCustomer(Klijent klijent) {
        super();
        odo = klijent;
        //validator
    }

    @Override
    protected void operation() throws Exception {
        dbbr.updateDomainObject(odo);
    }
}
