package logic;

import domain.DomainObject;

public class SOGenerateDomainObject extends SystemOperation {

    public SOGenerateDomainObject(DomainObject odo) {
        super();
        this.odo = odo;
        //validator
    }

    @Override
    protected void operation() throws Exception {
        odo = dbbr.initializeDomainObject(odo);
    }
}
