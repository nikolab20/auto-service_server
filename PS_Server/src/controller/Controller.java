/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;


import domain.DomainObject;
import domain.Klijent;
import logic.*;
import lombok.Getter;
import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

/**
 * @author nikol
 */

/**
 * Provides methods for controlling the logic of a server application.
 */
public class Controller {

    /**
     * The instance of the controller class.
     * The instance created on this site ensures its uniqueness for the whole project.
     *
     * @return the instance of the controller class.
     */
    @Getter
    private static Controller instance = new Controller();

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
     * A logical value that represents whether the server is set to the default configuration or not.
     *
     * @param defaultConfig is the boolean value for the attribute defaultConfig of this class.
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
     * @return An object of the {@link Properties} class that contains data from a file.
     * @throws IOException if file doesn't exist.
     */
    public Properties readPropertiesFile() throws IOException {
        FileInputStream in = new FileInputStream("conn.properties");
        Properties props = new Properties();
        props.load(in);

        return props;
    }

    /**
     * Method for initially adjusting the form.
     *
     * @param form      is the form tuned by this method.
     * @param mainPanel is the content panel of this form.
     */
    public void defaultPrepareForm(JFrame form, JPanel mainPanel, Dimension dimension) {
        form.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        form.setContentPane(mainPanel);
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
     * @param file  is the image file for the icon.
     * @param label is the label on which the method places the icon.
     */
    public void setIconToLabel(String file, JLabel label) {
        URL imageUrl = ClassLoader.getSystemResource(file);
        ImageIcon imageIcon = new ImageIcon(imageUrl);
        label.setIcon(imageIcon);
    }

    /**
     * Method for putting data into properties file.
     *
     * @param serverPort       is String that represents port for server.
     * @param driverDatabase   is String that represents database driver.
     * @param urlDatabase      is String that represents database url.
     * @param userDatabase     is String that represents database username.
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

    public DomainObject loginWorker(String username, String password) throws Exception {
        SystemOperation so = new SOWorkerLogin(username, password);
        so.execute();
        return so.getDomainObject();
    }

    public DomainObject createNewCustomer(DomainObject odo) throws Exception {
        SystemOperation so = new SOGenerateDomainObject(odo);
        so.execute();
        return so.getDomainObject();
    }

    public DomainObject addCustomer(Klijent klijent) throws Exception {
        SystemOperation so = new SOSaveNewCustomer(klijent);
        so.execute();
        return so.getDomainObject();
    }

    public List<DomainObject> searchCustomers(String criteria) throws Exception {
        SystemOperation so = new SOSearchCustomers(criteria);
        so.execute();
        return so.getListDomainObject();
    }

    public void updateCustomer(Klijent klijent) throws Exception {
        SystemOperation so = new SOUpdateCustomer(klijent);
        so.execute();
    }
}
