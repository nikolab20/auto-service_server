package logic;

/**
 *
 * @author nikol
 */
public class SOGetAllCustomers extends SystemOperation {

    /**
     * Default constructor without parameters which calls super class
     * constructor.
     */
    public SOGetAllCustomers() {
        super();
    }

    @Override
    protected void operation() throws Exception {
        listOdo = dbbr.selectAllCustomers();
    }
}
