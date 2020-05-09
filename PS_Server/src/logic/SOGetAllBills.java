package logic;

/**
 *
 * @author nikol
 */
public class SOGetAllBills extends SystemOperation {

    /**
     * Default constructor without parameters which calls super class
     * constructor.
     */
    public SOGetAllBills() {
        super();
    }

    @Override
    protected void operation() throws Exception {
        listOdo = dbbr.selectAllBill();
    }
}
