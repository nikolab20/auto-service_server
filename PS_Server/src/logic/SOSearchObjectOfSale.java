/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

/**
 *
 * @author nikol
 */
public class SOSearchObjectOfSale extends SystemOperation {

    private Long criteria;

    public SOSearchObjectOfSale(Long criteria) {
        super();
        this.criteria = criteria;
        //validator
    }

    @Override
    protected void operation() throws Exception {
        odo = dbbr.searchObjectOfSale(criteria);
    }
}
