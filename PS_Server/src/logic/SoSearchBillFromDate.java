/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.Date;

/**
 *
 * @author nikol
 */
public class SoSearchBillFromDate extends SystemOperation {

    private final Date date;

    public SoSearchBillFromDate(Date date) {
        this.date = date;
    }

    @Override
    protected void operation() throws Exception {
        listOdo = dbbr.searchBillFromDate(date);
    }

}
