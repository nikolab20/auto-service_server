/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import controller.Controller;
import domain.DomainObject;
import domain.Klijent;
import domain.Radnik;

import javax.sql.ConnectionPoolDataSource;
import javax.sql.DataSource;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * @author nikol
 */

/**
 * Provide communication between application and database.
 */
public class DatabaseBroker {

    /**
     * Represents connection between application and database.
     */
    private Connection connection;

    /**
     * Resource bundle for language packs.
     */
    private ResourceBundle languageBundle;

    /**
     * The constructor of this class without any parameters.
     */
    public DatabaseBroker() {
        this.languageBundle = ResourceBundle.getBundle("props/LanguageBundle", Controller.getInstance().getLocale());
    }

    /**
     * Method for making connection between application and database without parameters.
     *
     * @throws SQLException           if problems with connection to database arise.
     * @throws IOException            if streams are not open.
     * @throws ClassNotFoundException if driver for database isn't installed.
     */
    public void connect() throws SQLException, IOException, ClassNotFoundException {
        try {
            Properties props = Controller.getInstance().readPropertiesFile();

            String driver = props.getProperty("default_driverDatabase");
            String url = props.getProperty("default_urlDatabase");
            String user = props.getProperty("default_userDatabase");
            String password = props.getProperty("default_passwordDatabase");

            if (!Controller.getInstance().isDefaultConfig()) {
                driver = props.getProperty("driverDatabase");
                url = props.getProperty("urlDatabase");
                user = props.getProperty("userDatabase");
                password = props.getProperty("passwordDatabase");
            }

            Class.forName(driver);

            connection = DriverManager.getConnection(url, user, password);
            connection.setAutoCommit(false);
            System.out.println(languageBundle.getString("connectionSuccess"));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SQLException(languageBundle.getString("connectionProblem"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new FileNotFoundException(languageBundle.getString("problemsWithFile"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new ClassNotFoundException(languageBundle.getString("classNotFound"));
        } catch (IOException e) {
            e.printStackTrace();
            throw new IOException(languageBundle.getString("problemsWithStream"));
        }
    }

    /**
     * Method for disconnecting with the database.
     *
     * @throws SQLException if problems with disconnecting to database arise.
     */
    public void disconnect() throws SQLException {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new SQLException(languageBundle.getString("disconnectionProblem"));
            }
        }
    }

    /**
     * Method for verifying a transaction.
     *
     * @throws SQLException if problems with verifying to database arise.
     */
    public void commit() throws SQLException {
        if (connection != null) {
            try {
                connection.commit();
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new SQLException(languageBundle.getString("commitProblem"));
            }
        }
    }

    /**
     * Method to cancel a transaction.
     *
     * @throws SQLException if problems with canceling to database arise.
     */
    public void rollback() throws SQLException {
        if (connection != null) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
                throw new SQLException(languageBundle.getString("rollbackProblem"));
            }
        }
    }

    public DomainObject loginWorker(String username, String password) throws Exception {
        try {
            String query = "SELECT sifraRadnika, imeRadnika, prezimeRadnika, adresa, telefon, JMBG, administrator, " +
                    "username, password FROM radnik WHERE username = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet rs = statement.executeQuery();
            Radnik radnik = null;

            while (rs.next()) {
                radnik = new Radnik();
                radnik.setSifraRadnika(rs.getLong("sifraRadnika"));
                radnik.setImeRadnika(rs.getString("imeRadnika"));
                radnik.setPrezimeRadnika(rs.getString("prezimeRadnika"));
                radnik.setAdresa(rs.getString("adresa"));
                radnik.setTelefon(rs.getString("telefon"));
                radnik.setJMBG(rs.getString("JMBG"));
                radnik.setAdministrator(rs.getBoolean("administrator"));
                radnik.setUsername(rs.getString("username"));
                radnik.setPassword(rs.getString("password"));
            }

            return radnik;
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception();
        }
    }

    public DomainObject initializeDomainObject(DomainObject odo) throws Exception {
        try {
            String query = "INSERT INTO " + odo.getTableName() + " () VALUES ()";
            Statement statement = connection.createStatement();
            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

            ResultSet rs = statement.getGeneratedKeys();

            while (rs.next()) {
                Long id = rs.getLong(1);
                odo.setObjectId(id);
            }

            return odo;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception();
        }
    }

    public DomainObject updateDomainObject(DomainObject odo) throws Exception {
        try {
            String query = "UPDATE " + odo.getTableName() + " SET " + odo.getAttributesForUpdate() +
                    "WHERE " + odo.getIdentifierName() + " = " + odo.getObjectId();

            Statement statement = connection.createStatement();
            statement.executeUpdate(query);

            return odo;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception();
        }
    }

    public List<DomainObject> searchCustomer(String criteria) throws Exception {

        try {
            String query = "SELECT * FROM klijent WHERE imeKlijenta LIKE '" + criteria +
                    "' OR prezimeKlijenta LIKE '" + criteria + "' OR sifraKlijenta = '" + criteria + "'";

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            List<DomainObject> customers = new ArrayList<>();

            while (rs.next()) {
                Klijent klijent = new Klijent();
                klijent.setSifraKlijenta(rs.getLong("sifraKlijenta"));
                klijent.setImeKlijenta(rs.getString("imeKlijenta"));
                klijent.setPrezimeKlijenta(rs.getString("prezimeKlijenta"));
                klijent.setBrojPoseta(rs.getInt("brojPoseta"));
                klijent.setDug(rs.getBigDecimal("dug"));
                customers.add(klijent);
            }

            return customers;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception();
        }
    }
}
