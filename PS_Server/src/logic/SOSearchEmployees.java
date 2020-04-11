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
public class SOSearchEmployees extends SystemOperation {

    private String criteria;
    
    public SOSearchEmployees(String criteria) {
        super();
        this.criteria = criteria;
        //validator
    }
    
    @Override
    protected void operation() throws Exception {
        listOdo = dbbr.searchEmployees(criteria);
    }
    
}
