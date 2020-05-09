package logic;

/**
 *
 * @author nikol
 */
public class SOSearchClientsWithDebt extends SystemOperation {

    /**
     * Default constructor without parameters which calls super class
     * constructor.
     */
    public SOSearchClientsWithDebt() {
        super();
    }

    @Override
    protected void operation() throws Exception {
        listOdo = dbbr.searchClientsWithDebt();
    }
}
