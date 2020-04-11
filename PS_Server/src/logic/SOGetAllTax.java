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
public class SOGetAllTax extends SystemOperation {

    public SOGetAllTax() {
        super();
        //validator
    }

    @Override
    protected void operation() throws Exception {
        listOdo = dbbr.selectAllTax();
    }
}
