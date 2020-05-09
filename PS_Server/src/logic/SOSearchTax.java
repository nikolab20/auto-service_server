package logic;

/**
 *
 * @author nikol
 */
public class SOSearchTax extends SystemOperation {

    /**
     * Criteria for tax rates search.
     */
    private final Long id;

    /**
     * Parameterized constructor for this system operation.
     *
     * @param criteria is criteria for search.
     */
    public SOSearchTax(Long id) {
        super();
        this.id = id;
    }

    @Override
    protected void operation() throws Exception {
        listOdo = dbbr.searchTax(id);
    }
}
