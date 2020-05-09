package logic;

import domain.DomainObject;

public class SOUpdateDomainObject extends SystemOperation {

    /**
     * Parameterized constructor for this system operation.
     * 
     * @param odo is customer that user need to update.
     */
    public SOUpdateDomainObject(DomainObject odo) {
        super();
        this.odo = odo;
    }

    @Override
    protected void operation() throws Exception {
        odo = dbbr.updateDomainObject(odo);
    }
}
