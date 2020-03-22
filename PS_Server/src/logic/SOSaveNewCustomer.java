package logic;

import domain.Klijent;

public class SOSaveNewCustomer extends SystemOperation {

    public SOSaveNewCustomer(Klijent klijent) {
        super();
        odo = klijent;
        //validator
    }

    @Override
    protected void operation() throws Exception {
        odo = dbbr.updateDomainObject(odo);
    }
}
