package logic;

import domain.DomainObject;
import domain.Klijent;

public class SOUpdateDomainObject extends SystemOperation {

    public SOUpdateDomainObject(DomainObject odo) {
        super();
        this.odo = odo;
        //validator
    }

    @Override
    protected void operation() throws Exception {
        odo = dbbr.updateDomainObject(odo);
    }
}
