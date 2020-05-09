package logic;

/**
 *
 * @author nikol
 */
public class SOGetAllTax extends SystemOperation {

    /**
     * Default constructor without parameters which calls super class
     * constructor.
     */
    public SOGetAllTax() {
        super();
    }

    @Override
    protected void operation() throws Exception {
        listOdo = dbbr.selectAllTax();
    }
}
