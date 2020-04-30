package logic;

import domain.DomainObject;

public class SOUpdateDomainObject extends SystemOperation {

    public SOUpdateDomainObject(DomainObject odo) {
        super();
        this.odo = odo;
    }

    @Override
    protected void operation() throws Exception {
        odo = dbbr.updateDomainObject(odo);
    }
}
