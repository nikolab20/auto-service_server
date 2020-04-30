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

    private final Long employeeID;

    public SOSearchEmployees(Long employeeID) {
        super();
        this.employeeID = employeeID;
    }

    @Override
    protected void operation() throws Exception {
        listOdo = dbbr.searchEmployees(employeeID);
    }

}
