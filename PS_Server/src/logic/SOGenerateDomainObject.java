package logic;

import domain.DomainObject;

public class SOGenerateDomainObject extends SystemOperation {

    /**
     * Parameterized constructor for this system operation.
     *
     * @param odo is object that user need to initialize.
     */
    public SOGenerateDomainObject(DomainObject odo) {
        super();
        this.odo = odo;
    }

    @Override
    protected void operation() throws Exception {
        odo = dbbr.initializeDomainObject(odo);
    }
}
