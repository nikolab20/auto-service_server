package logic;

/**
 *
 * @author nikol
 */
public class SOGetAllOfObjectOfSale extends SystemOperation {

    /**
     * Default constructor without parameters which calls super class
     * constructor.
     */
    public SOGetAllOfObjectOfSale() {
        super();
    }

    @Override
    protected void operation() throws Exception {
        mapOdo = dbbr.getAllObjectOfSaleWithNames();
    }
}
