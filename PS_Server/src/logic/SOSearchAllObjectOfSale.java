package logic;

/**
 *
 * @author nikol
 */
public class SOSearchAllObjectOfSale extends SystemOperation {

    /**
     * Criteria for objects of sale search.
     */
    private final String criteria;

    /**
     * Parameterized constructor for this system operation.
     *
     * @param criteria is criteria for search.
     */
    public SOSearchAllObjectOfSale(String criteria) {
        super();
        this.criteria = criteria;
    }

    @Override
    protected void operation() throws Exception {
        mapOdo = dbbr.getAllObjectOfSaleWithNames();
    }
}
