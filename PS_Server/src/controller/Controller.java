/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import domain.DomainObject;
import logic.*;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

/**
 * @author nikol
 */
/**
 * Provides methods for controlling the logic of a server application.
 */
public class Controller {

    /**
     * The instance of the controller class. The instance created on this site
     * ensures its uniqueness for the whole project.
     *
     * @return the instance of the controller class.
     */
    @Getter
    private final static Controller instance = new Controller();

    /**
     * Definition of language region.
     *
     * @param locale is the value for the attribute locale of this class.
     * @return the object of class Locale.
     */
    @Getter
    @Setter
    private Locale locale;

    /**
     * A logical value that represents whether the server is set to the default
     * configuration or not.
     *
     * @param defaultConfig is the boolean value for the attribute defaultConfig
     * of this class.
     * @return the boolean value.
     */
    @Getter
    @Setter
    private boolean defaultConfig;

    /**
     * The constructor of this class without any parameters.
     */
    private Controller() {

    }

    /**
     * Method for reading a data from properties file.
     *
     * @return An object of the {@link Properties} class that contains data from
     * a file.
     * @throws IOException if file doesn't exist.
     */
    public Properties readPropertiesFile() throws IOException {
        FileInputStream in = new FileInputStream("props/conn.properties");
        Properties props = new Properties();
        props.load(in);

        return props;
    }

    /**
     * Method for initial adjusting the form.
     *
     * @param form is the form tuned by this method.
     * @param dimension is the value of dimension of form.
     */
    public void defaultPrepareForm(JFrame form, Dimension dimension) {
        form.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        form.setPreferredSize(dimension);
        form.pack();
        form.setLocationRelativeTo(null);
        URL imageUrl = ClassLoader.getSystemResource("img/transportation.png");
        ImageIcon icon = new ImageIcon(imageUrl);
        form.setIconImage(icon.getImage());
    }

    /**
     * Method for putting icon on label.
     *
     * @param file is the image file for the icon.
     * @param label is the label on which the method places the icon.
     */
    public void setIconToLabel(String file, JLabel label) {
        URL imageUrl = ClassLoader.getSystemResource(file);
        ImageIcon imageIcon = new ImageIcon(imageUrl);
        label.setIcon(imageIcon);
    }

    /**
     * Method for putting icon on button.
     *
     * @param file is the image file for the icon.
     * @param button is the button on which the method places the icon.
     */
    public void setIconToButton(String file, JButton button) {
        URL imageUrl = ClassLoader.getSystemResource(file);
        ImageIcon imageIcon = new ImageIcon(imageUrl);
        button.setIcon(imageIcon);
    }

    /**
     * Method for putting data into properties file.
     *
     * @param serverPort is String that represents port for server.
     * @param driverDatabase is String that represents database driver.
     * @param urlDatabase is String that represents database url.
     * @param userDatabase is String that represents database username.
     * @param passwordDatabase is String that represents database password.
     * @throws IOException if file doesn't exist.
     */
    public void writeIntoPropertiesFile(String serverPort, String driverDatabase, String urlDatabase, String userDatabase, String passwordDatabase) throws IOException {
        FileInputStream in = new FileInputStream("conn.properties");
        Properties props = new Properties();
        props.load(in);

        props.setProperty("serverPort", serverPort);
        props.setProperty("driverDatabase", driverDatabase);
        props.setProperty("urlDatabase", urlDatabase);
        props.setProperty("userDatabase", userDatabase);
        props.setProperty("passwordDatabase", passwordDatabase);
    }

    /**
     * Method for calling system operation for worker login.
     *
     * @param username is String that represents worker username.
     * @param password is String that represents worker password.
     * @return an instance of DomainObject class that represents logged worker.
     * @throws Exception if can't work with database.
     */
    public DomainObject loginWorker(String username, String password) throws Exception {
        SystemOperation so = new SOWorkerLogin(username, password);
        so.execute();
        return so.getDomainObject();
    }

    /**
     * Method for calling system operation for create new domain object.
     *
     * @param odo is DomainObject that represents empty instance of domain
     * object instance.
     * @return an instance of DomainObject with the identifier set
     * @throws Exception if can't work with database.
     */
    public DomainObject createNewDomainObject(DomainObject odo) throws Exception {
        SystemOperation so = new SOGenerateDomainObject(odo);
        so.execute();
        return so.getDomainObject();
    }

    /**
     * Method for calling system operation for insert new domain object.
     *
     * @param odo is DomainObject that represents instance of domain object
     * instance.
     * @return an instance of DomainObject which is inserted into the database.
     * @throws Exception if can't work with database.
     */
    public DomainObject insertDomainObject(DomainObject odo) throws Exception {
        SystemOperation so = new SOInsertDomainObject(odo);
        so.execute();
        return so.getDomainObject();
    }

    /**
     * Method for calling system operation for customer search.
     *
     * @param customerID is Long that represents criteria for customer search.
     * @return list of search clients.
     * @throws Exception if can't work with database.
     */
    public List<DomainObject> searchCustomers(Long customerID) throws Exception {
        SystemOperation so = new SOSearchCustomers(customerID);
        so.execute();
        return so.getListDomainObject();
    }

    /**
     * Method for calling system operation for update domain object.
     *
     * @param odo is DomainObject that represents instance of domain object.
     * @return an instance of DomainObject which is updated in the database.
     * @throws Exception if can't work with database.
     */
    public DomainObject updateDomainObject(DomainObject odo) throws Exception {
        SystemOperation so = new SOUpdateDomainObject(odo);
        so.execute();
        return so.getDomainObject();
    }

    /**
     * Method for calling system operation for employees search.
     *
     * @param employeeID is Long that represents criteria for employees search.
     * @return list of search employees.
     * @throws Exception if can't work with database.
     */
    public List<DomainObject> searchEmployees(Long employeeID) throws Exception {
        SystemOperation so = new SOSearchEmployees(employeeID);
        so.execute();
        return so.getListDomainObject();
    }

    /**
     * Method for calling system operation for delete domain object.
     *
     * @param odo is DomainObject that represents instance of domain object.
     * @return an instance of DomainObject which is deleted in the database.
     * @throws Exception if can't work with database.
     */
    public DomainObject deleteDomainObject(DomainObject odo) throws Exception {
        SystemOperation so = new SODeleteDomainObject(odo);
        so.execute();
        return so.getDomainObject();
    }

    /**
     * Method for calling system operation that returns all tax from database.
     *
     * @return list of tax.
     * @throws Exception if can't work with database.
     */
    public List<DomainObject> getAllTax() throws Exception {
        SystemOperation so = new SOGetAllTax();
        so.execute();
        return so.getListDomainObject();
    }

    /**
     * Method for calling system operation for car parts search.
     *
     * @param criteria is String that represents criteria for car parts search.
     * @return list of search car parts.
     * @throws Exception if can't work with database.
     */
    public List<DomainObject> searchCarParts(Long criteria) throws Exception {
        SystemOperation so = new SOSearchCarParts(criteria);
        so.execute();
        return so.getListDomainObject();
    }

    /**
     * Method for calling system operation for service search.
     *
     * @param criteria is String that represents criteria for service search.
     * @return list of search service.
     * @throws Exception if can't work with database.
     */
    public List<DomainObject> searchService(Long criteria) throws Exception {
        SystemOperation so = new SOSearchService(criteria);
        so.execute();
        return so.getListDomainObject();
    }

    public DomainObject searchObjectOfSale(Long criteria) throws Exception {
        SystemOperation so = new SOSearchObjectOfSale(criteria);
        so.execute();
        return so.getDomainObject();
    }

    public Map<Object, Object> searchObjectOfSaleWithNames(Long criteria) throws Exception {
        SystemOperation so = new SOSearchObjectOfSale(criteria);
        so.execute();
        return so.getMapDomainObject();
    }

    public void insertListOfDomainObject(List<DomainObject> listOdo) throws Exception {
        SystemOperation so = new SOInsertListOfDomainObject(listOdo);
        so.execute();
    }

    public List<DomainObject> searchBill(Long criteria) throws Exception {
        SystemOperation so = new SOSearchBill(criteria);
        so.execute();
        return so.getListDomainObject();
    }

    public List<DomainObject> searchBillFromDate(Date date) throws Exception {
        SystemOperation so = new SoSearchBillFromDate(date);
        so.execute();
        return so.getListDomainObject();
    }

    public List<DomainObject> searchNewClientsFromDate(Date date) throws Exception {
        SystemOperation so = new SOSearchNewClientsFromDate(date);
        so.execute();
        return so.getListDomainObject();
    }

    public List<DomainObject> searchClientsWithDebt() throws Exception {
        SystemOperation so = new SOSearchClientsWithDebt();
        so.execute();
        return so.getListDomainObject();
    }

    public List<DomainObject> searchTax(Long id) throws Exception {
        SystemOperation so = new SOSearchTax(id);
        so.execute();
        return so.getListDomainObject();
    }

    public List<DomainObject> getAllEmployees() throws Exception {
        SystemOperation so = new SOGetAllEmployees();
        so.execute();
        return so.getListDomainObject();
    }

    public List<DomainObject> getAllCarParts() throws Exception {
        SystemOperation so = new SOGetAllCarParts();
        so.execute();
        return so.getListDomainObject();
    }

    public List<DomainObject> getAllServices() throws Exception {
        SystemOperation so = new SOGetAllServices();
        so.execute();
        return so.getListDomainObject();
    }

    public List<DomainObject> getAllBills() throws Exception {
        SystemOperation so = new SOGetAllBills();
        so.execute();
        return so.getListDomainObject();
    }

    public List<DomainObject> getAllCustomers() throws Exception {
        SystemOperation so = new SOGetAllCustomers();
        so.execute();
        return so.getListDomainObject();
    }

    public Map<Object, Object> getAllObjectOfSale() throws Exception {
        SystemOperation so = new SOGetAllOfObjectOfSale();
        so.execute();
        return so.getMapDomainObject();
    }

    public DomainObject checkUsername(String username) throws Exception {
        SystemOperation so = new SOCheckUsername(username);
        so.execute();
        return so.getDomainObject();
    }
}
