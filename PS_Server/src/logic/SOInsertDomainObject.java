package logic;

import domain.DomainObject;

/**
 *
 * @author nikol
 */
public class SOInsertDomainObject extends SystemOperation {

    /**
     * Parameterized constructor for this system operation.
     *
     * @param odo is object that user need to insert.
     */
    public SOInsertDomainObject(DomainObject odo) {
        super();
        this.odo = odo;
    }

    @Override
    protected void operation() throws Exception {
        odo = dbbr.insertDomainObject(odo);
    }
}
