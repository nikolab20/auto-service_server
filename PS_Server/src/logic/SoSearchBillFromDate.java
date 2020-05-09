package logic;

import java.util.Date;

/**
 *
 * @author nikol
 */
public class SoSearchBillFromDate extends SystemOperation {

    /**
     * Criteria for bills search.
     */
    private final Date date;

    /**
     * Parameterized constructor for this system operation.
     *
     * @param date is criteria for search.
     */
    public SoSearchBillFromDate(Date date) {
        this.date = date;
    }

    @Override
    protected void operation() throws Exception {
        listOdo = dbbr.searchBillFromDate(date);
    }

}
