package logic;

/**
 *
 * @author nikol
 */
public class SOSearchObjectOfSale extends SystemOperation {

    /**
     * Criteria for objects of sale search.
     */
    private final Long criteria;

    /**
     * Parameterized constructor for this system operation.
     *
     * @param criteria is criteria for search.
     */
    public SOSearchObjectOfSale(Long criteria) {
        super();
        this.criteria = criteria;
    }

    @Override
    protected void operation() throws Exception {
        odo = dbbr.searchObjectOfSale(criteria);
    }
}
