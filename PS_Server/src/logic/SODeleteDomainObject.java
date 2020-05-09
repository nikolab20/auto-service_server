package logic;

import domain.DomainObject;

/**
 *
 * @author nikol
 */
public class SODeleteDomainObject extends SystemOperation {

    /**
     * Parameterized constructor for this system operation.
     *
     * @param odo is object that user need to delete.
     */
    public SODeleteDomainObject(DomainObject odo) {
        super();
        this.odo = odo;
    }

    @Override
    protected void operation() throws Exception {
        odo = dbbr.deleteDomainObject(odo);
    }
}
