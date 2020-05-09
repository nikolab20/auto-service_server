package database;

import controller.Controller;
import domain.Deo;
import domain.DomainObject;
import domain.Klijent;
import domain.PoreskaStopa;
import domain.PredmetProdaje;
import domain.Racun;
import domain.Radnik;
import domain.Usluga;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final ResourceBundle resourceBundle;

    /**
     * The constructor of this class without any parameters.
     */
    public DatabaseBroker() {
        this.resourceBundle = ResourceBundle.getBundle("props/LanguageBundle", Controller.getInstance().getLocale());
    }

    /**
     * Method for making connection between application and database without
     * parameters.
     *
     * @throws SQLException if problems with connection to database arise.
     * @throws IOException if streams are not open.
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
        } catch (SQLException e) {
            throw new SQLException(resourceBundle.getString("database_broker_connection_problem"));
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(resourceBundle.getString("database_broker_file_problem"));
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException(resourceBundle.getString("database_broker_driver_problem"));
        } catch (IOException e) {
            throw new IOException(resourceBundle.getString("database_broker_stream_problem"));
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
                throw new SQLException(resourceBundle.getString("database_broker_diconnection_problem"));
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
                throw new SQLException(resourceBundle.getString("database_broker_commit_problem"));
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
                throw new SQLException(resourceBundle.getString("database_broker_roolback_problem"));
            }
        }
    }

    /**
     * Method for search users from a database by specific username and
     * password.
     *
     * @param username is represents username of requested user.
     * @param password is represents password of requested user.
     * @return object of Radnik class.
     * @throws Exception if there are database connection problems.
     */
    public synchronized DomainObject loginWorker(String username, String password) throws Exception {
        try {
            String query = "SELECT sifraRadnika, imeRadnika, prezimeRadnika, adresa, telefon, JMBG, administrator, "
                    + "username, password FROM radnik WHERE username = ? AND password = ?";
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
        } catch (SQLException ex) {
            throw new Exception(resourceBundle.getString("database_select_exception") + " radnik");
        }

    }

    /**
     * Method for initialization objects into database.
     *
     * @param odo is represents object that user need to initialize.
     * @return object with ID set.
     * @throws Exception if there are database connection problems.
     */
    public DomainObject initializeDomainObject(DomainObject odo) throws Exception {
        try {
            String query = "INSERT INTO " + odo.getTableName() + " () VALUES ()";
            Statement statement = connection.createStatement();
            statement.execute(query, Statement.RETURN_GENERATED_KEYS);

            ResultSet rs = statement.getGeneratedKeys();

            while (rs.next()) {
                Long id = rs.getLong(1);
                odo.setObjectId(id);
            }

            return odo;
        } catch (SQLException e) {
            throw new Exception(resourceBundle.getString("database_insert_exception") + " " + odo.getTableName());
        }
    }

    /**
     * Method for updating objects from a database.
     *
     * @param odo is represents object that user need to update.
     * @return updated object.
     * @throws Exception if there are database connection problems.
     */
    public synchronized DomainObject updateDomainObject(DomainObject odo) throws Exception {
        try {
            String query = "UPDATE " + odo.getTableName() + " SET "
                    + odo.getAttributesForUpdate() + " WHERE " + odo.getIdentifierName() + " = "
                    + odo.getObjectId();
            Statement statement = connection.createStatement();
            statement.executeUpdate(query);

            return odo;
        } catch (SQLException e) {
            throw new Exception(resourceBundle.getString("database_update_exception") + " " + odo.getTableName());
        }
    }

    /**
     * Method for searching clients in a database.
     *
     * @param customerID is represents criteria for search.
     * @return list of customers.
     * @throws Exception if there are database connection problems.
     */
    public List<DomainObject> searchCustomer(Long customerID) throws Exception {
        try {
            String query = "SELECT sifraKlijenta, imeKlijenta, prezimeKlijenta, brojPoseta, dug "
                    + "FROM klijent WHERE sifraKlijenta = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, customerID);
            ResultSet rs = statement.executeQuery();
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
            throw new Exception(resourceBundle.getString("database_select_exception") + " klijent");
        }
    }

    /**
     * Method for searching employees in a database.
     *
     * @param employeeID is represents criteria for search.
     * @return list of employees.
     * @throws Exception if there are database connection problems.
     */
    public List<DomainObject> searchEmployees(Long employeeID) throws Exception {
        try {
            String query = "SELECT sifraRadnika, imeRadnika, prezimeRadnika, adresa, "
                    + "telefon, JMBG, administrator, username, password FROM radnik "
                    + "WHERE sifraRadnika = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, employeeID);
            ResultSet rs = statement.executeQuery();
            List<DomainObject> radnici = new ArrayList<>();

            while (rs.next()) {
                Radnik radnik = new Radnik();
                radnik.setSifraRadnika(rs.getLong("sifraRadnika"));
                radnik.setImeRadnika(rs.getString("imeRadnika"));
                radnik.setPrezimeRadnika(rs.getString("prezimeRadnika"));
                radnik.setAdresa(rs.getString("adresa"));
                radnik.setTelefon(rs.getString("telefon"));
                radnik.setJMBG(rs.getString("JMBG"));
                radnik.setAdministrator(rs.getBoolean("administrator"));
                radnik.setUsername(rs.getString("username"));
                radnik.setPassword(rs.getString("password"));
                radnici.add(radnik);
            }

            return radnici;

        } catch (SQLException e) {
            throw new Exception(resourceBundle.getString("database_select_exception") + " radnik");
        }
    }

    /**
     * Method for deleting objects from a database.
     *
     * @param odo is represents object that user need to delete.
     * @return deleted object.
     * @throws Exception if there are database connection problems.
     */
    public synchronized DomainObject deleteDomainObject(DomainObject odo) throws Exception {
        try {
            String query = "DELETE FROM " + odo.getTableName() + " WHERE " + odo.getIdentifierName() + " = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, odo.getObjectId());
            statement.executeUpdate();

            return odo;

        } catch (SQLException e) {
            throw new Exception(resourceBundle.getString("database_delete_exception") + " " + odo.getTableName());
        }
    }

    /**
     * Method for inserting objects from a database.
     *
     * @param odo is represents object that user need to insert.
     * @return inserted object.
     * @throws Exception if there are database connection problems.
     */
    public DomainObject insertDomainObject(DomainObject odo) throws Exception {
        try {
            String query = "INSERT INTO " + odo.getTableName() + " ("
                    + odo.getAttributeNamesForInsert() + ") VALUES ("
                    + odo.getAttributeValuesForInsert() + ")";

            Statement statement = connection.prepareStatement(query);
            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = statement.getGeneratedKeys();

            if (odo.isAutoincrement()) {
                Long id = null;
                while (rs.next()) {
                    id = rs.getLong(1);
                }
                odo.setObjectId(id);
            }

            return odo;

        } catch (SQLException e) {
            throw new Exception(resourceBundle.getString("database_insert_exception") + " " + odo.getTableName());
        }
    }

    /**
     * Method for getting all tax rates from a database.
     *
     * @return a list of tax rates.
     * @throws Exception if there are database connection problems.
     */
    public List<DomainObject> selectAllTax() throws Exception {
        try {
            String query = "SELECT id, oznaka, vrednost FROM poreskastopa";

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            List<DomainObject> poreskeStope = new ArrayList<>();

            while (rs.next()) {
                PoreskaStopa ps = new PoreskaStopa();

                ps.setId(rs.getLong("id"));
                ps.setOznaka(rs.getString("oznaka"));
                ps.setVrednost(rs.getBigDecimal("vrednost"));

                poreskeStope.add(ps);
            }

            return poreskeStope;

        } catch (SQLException e) {
            throw new Exception(resourceBundle.getString("database_select_exception") + " poreskastopa");
        }
    }

    /**
     * Method for getting all customers from a database.
     *
     * @return a list of customers.
     * @throws Exception if there are database connection problems.
     */
    public List<DomainObject> selectAllCustomers() throws Exception {
        try {
            String query = "SELECT sifraKlijenta, imeKlijenta, prezimeKlijenta, brojPoseta, dug FROM klijent";

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
            throw new Exception(resourceBundle.getString("database_select_exception") + " klijent");
        }
    }

    /**
     * Method for getting all employees from a database.
     *
     * @return a list of employees.
     * @throws Exception if there are database connection problems.
     */
    public List<DomainObject> selectAllEmployees() throws Exception {
        try {
            String query = "SELECT sifraRadnika, imeRadnika, prezimeRadnika, adresa, "
                    + "telefon, JMBG, administrator, username, password FROM radnik";

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            List<DomainObject> radnici = new ArrayList<>();

            while (rs.next()) {
                Radnik radnik = new Radnik();
                radnik.setSifraRadnika(rs.getLong("sifraRadnika"));
                radnik.setImeRadnika(rs.getString("imeRadnika"));
                radnik.setPrezimeRadnika(rs.getString("prezimeRadnika"));
                radnik.setAdresa(rs.getString("adresa"));
                radnik.setTelefon(rs.getString("telefon"));
                radnik.setJMBG(rs.getString("JMBG"));
                radnik.setAdministrator(rs.getBoolean("administrator"));
                radnik.setUsername(rs.getString("username"));
                radnik.setPassword(rs.getString("password"));
                radnici.add(radnik);
            }

            return radnici;

        } catch (SQLException e) {
            throw new Exception(resourceBundle.getString("database_select_exception") + " radnik");
        }
    }

    /**
     * Method for getting all car parts from a database.
     *
     * @return a list of car parts.
     * @throws Exception if there are database connection problems.
     */
    public List<DomainObject> selectAllCarParts() throws Exception {
        try {
            String query = "SELECT d.serijskiBroj, d.nazivDela, d.proizvodjac, "
                    + "d.opis, d.sifraPredmetaProdaje, d.stanje, pp.sifraPredmetaProdaje, "
                    + "pp.cena, pp.cenaSaPorezom, pp.id, ps.id, ps.oznaka, ps.vrednost "
                    + "FROM deo d JOIN predmetProdaje pp on (d.sifraPredmetaProdaje = pp.sifraPredmetaProdaje) "
                    + "JOIN poreskaStopa ps on (pp.id = ps.id)";

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            List<DomainObject> parts = new ArrayList<>();

            while (rs.next()) {
                PoreskaStopa poreskaStopa = new PoreskaStopa();
                poreskaStopa.setId(rs.getLong("ps.id"));
                poreskaStopa.setVrednost(rs.getBigDecimal("ps.vrednost"));
                poreskaStopa.setOznaka(rs.getString("ps.oznaka"));

                PredmetProdaje predmetProdaje = new PredmetProdaje();
                predmetProdaje.setSifraPredmetaProdaje(rs.getLong("pp.sifraPredmetaProdaje"));
                predmetProdaje.setCena(rs.getBigDecimal("pp.cena"));
                predmetProdaje.setCenaSaPorezom(rs.getBigDecimal("pp.cenaSaPorezom"));
                predmetProdaje.setPoreskaStopa(poreskaStopa);

                Deo deo = new Deo();
                deo.setPredmetProdaje(predmetProdaje);
                deo.setSerijskiBroj(rs.getLong("serijskiBroj"));
                deo.setNazivDela(rs.getString("nazivDela"));
                deo.setProizvodjac(rs.getString("proizvodjac"));
                deo.setOpis(rs.getString("opis"));
                deo.setStanje(rs.getInt("stanje"));

                parts.add(deo);
            }

            return parts;

        } catch (SQLException e) {
            throw new Exception(resourceBundle.getString("database_select_exception") + " deo");
        }
    }

    /**
     * Method for getting all services from a database.
     *
     * @return a list of services.
     * @throws Exception if there are database connection problems.
     */
    public List<DomainObject> selectAllServices() throws Exception {
        try {
            String query = "SELECT u.sifraUsluge, u.nazivUsluge, u.opisUsluge, u.sifraPredmetaProdaje, pp.sifraPredmetaProdaje, "
                    + "pp.cena, pp.cenaSaPorezom, pp.id, ps.id, ps.oznaka, ps.vrednost "
                    + "FROM usluga u JOIN predmetProdaje pp on (u.sifraPredmetaProdaje = pp.sifraPredmetaProdaje) "
                    + "JOIN poreskaStopa ps on (pp.id = ps.id)";

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            List<DomainObject> services = new ArrayList<>();

            while (rs.next()) {
                PoreskaStopa poreskaStopa = new PoreskaStopa();
                poreskaStopa.setId(rs.getLong("ps.id"));
                poreskaStopa.setVrednost(rs.getBigDecimal("ps.vrednost"));
                poreskaStopa.setOznaka(rs.getString("ps.oznaka"));

                PredmetProdaje predmetProdaje = new PredmetProdaje();
                predmetProdaje.setSifraPredmetaProdaje(rs.getLong("pp.sifraPredmetaProdaje"));
                predmetProdaje.setCena(rs.getBigDecimal("pp.cena"));
                predmetProdaje.setCenaSaPorezom(rs.getBigDecimal("pp.cenaSaPorezom"));
                predmetProdaje.setPoreskaStopa(poreskaStopa);

                Usluga usluga = new Usluga();
                usluga.setPredmetProdaje(predmetProdaje);
                usluga.setNazivUsluge(rs.getString("u.nazivUsluge"));
                usluga.setOpisUsluge(rs.getString("u.opisUsluge"));
                usluga.setSifraUsluge(rs.getLong("u.sifraUsluge"));

                services.add(usluga);
            }

            return services;

        } catch (SQLException e) {
            throw new Exception(resourceBundle.getString("database_select_exception") + " usluga");
        }
    }

    /**
     * Method for getting all bills from a database.
     *
     * @return a list of bills.
     * @throws Exception if there are database connection problems.
     */
    public List<DomainObject> selectAllBill() throws Exception {
        try {
            String query = "SELECT r.brojRacuna, r.datumIzdavanja, r.ukupnaVrednost,"
                    + " r.ukupnaVrednostSaPorezom, r.obradjen, r.storniran, ra.sifraRadnika, "
                    + "ra.imeRadnika, ra.PrezimeRadnika, ra.adresa, ra.telefon, ra.JMBG, "
                    + "ra.administrator, ra.username, ra.password, k.sifraKlijenta, k.imeKlijenta, "
                    + "k.prezimeKlijenta, k.brojPoseta, k.dug FROM racun r JOIN radnik ra ON "
                    + "(r.sifraRadnika = ra.sifraRadnika) JOIN klijent k ON "
                    + "(r.sifraKlijenta = k.sifraKlijenta)";

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            List<DomainObject> bills = new ArrayList<>();

            while (rs.next()) {
                Klijent klijent = new Klijent();
                klijent.setSifraKlijenta(rs.getLong("k.sifraKlijenta"));
                klijent.setImeKlijenta(rs.getString("k.imeKlijenta"));
                klijent.setPrezimeKlijenta(rs.getString("k.prezimeKlijenta"));
                klijent.setBrojPoseta(rs.getInt("k.brojPoseta"));
                klijent.setDug(rs.getBigDecimal("k.dug"));

                Radnik radnik = new Radnik();
                radnik.setSifraRadnika(rs.getLong("ra.sifraRadnika"));
                radnik.setImeRadnika(rs.getString("ra.imeRadnika"));
                radnik.setPrezimeRadnika(rs.getString("ra.prezimeRadnika"));
                radnik.setAdresa(rs.getString("ra.adresa"));
                radnik.setTelefon(rs.getString("ra.telefon"));
                radnik.setJMBG(rs.getString("ra.JMBG"));
                radnik.setAdministrator(rs.getBoolean("ra.administrator"));
                radnik.setUsername(rs.getString("ra.username"));
                radnik.setPassword(rs.getString("ra.password"));

                Racun racun = new Racun();
                racun.setBrojRacuna(rs.getLong("r.brojRacuna"));
                racun.setDatumIzdavanja(new java.util.Date(rs.getDate("r.datumIzdavanja").getTime()));
                racun.setUkupnaVrednost(rs.getBigDecimal("r.ukupnaVrednost"));
                racun.setUkupnaVrednostSaPorezom(rs.getBigDecimal("r.ukupnaVrednostSaPorezom"));
                racun.setObradjen(rs.getBoolean("r.obradjen"));
                racun.setStorniran(rs.getBoolean("r.storniran"));
                racun.setKlijent(klijent);
                racun.setRadnik(radnik);

                bills.add(racun);
            }

            return bills;
        } catch (SQLException ex) {
            throw new Exception(resourceBundle.getString("database_select_exception") + " racun");
        }
    }

    /**
     * Method for searching cart parts in a database.
     *
     * @param criteria is represents criteria for search.
     * @return list of car parts.
     * @throws Exception if there are database connection problems.
     */
    public List<DomainObject> searchCarPart(Long criteria) throws Exception {
        try {
            String query = "SELECT d.serijskiBroj, d.nazivDela, d.proizvodjac, "
                    + "d.opis, d.sifraPredmetaProdaje, d.stanje, pp.sifraPredmetaProdaje, "
                    + "pp.cena, pp.cenaSaPorezom, pp.id, ps.id, ps.oznaka, ps.vrednost "
                    + "FROM deo d JOIN predmetProdaje pp on (d.sifraPredmetaProdaje = pp.sifraPredmetaProdaje) "
                    + "JOIN poreskaStopa ps on (pp.id = ps.id) WHERE d.serijskiBroj = ?"
                    + " OR d.sifraPredmetaProdaje = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, criteria);
            statement.setLong(2, criteria);
            ResultSet rs = statement.executeQuery();
            List<DomainObject> parts = new ArrayList<>();

            while (rs.next()) {
                PoreskaStopa poreskaStopa = new PoreskaStopa();
                poreskaStopa.setId(rs.getLong("ps.id"));
                poreskaStopa.setVrednost(rs.getBigDecimal("ps.vrednost"));
                poreskaStopa.setOznaka(rs.getString("ps.oznaka"));

                PredmetProdaje predmetProdaje = new PredmetProdaje();
                predmetProdaje.setSifraPredmetaProdaje(rs.getLong("pp.sifraPredmetaProdaje"));
                predmetProdaje.setCena(rs.getBigDecimal("pp.cena"));
                predmetProdaje.setCenaSaPorezom(rs.getBigDecimal("pp.cenaSaPorezom"));
                predmetProdaje.setPoreskaStopa(poreskaStopa);

                Deo deo = new Deo();
                deo.setPredmetProdaje(predmetProdaje);
                deo.setSerijskiBroj(rs.getLong("serijskiBroj"));
                deo.setNazivDela(rs.getString("nazivDela"));
                deo.setProizvodjac(rs.getString("proizvodjac"));
                deo.setOpis(rs.getString("opis"));
                deo.setStanje(rs.getInt("stanje"));

                parts.add(deo);
            }

            return parts;

        } catch (SQLException e) {
            throw new Exception(resourceBundle.getString("database_select_exception") + " deo");
        }
    }

    /**
     * Method for searching services in a database.
     *
     * @param criteria is represents criteria for search.
     * @return list of services.
     * @throws Exception if there are database connection problems.
     */
    public List<DomainObject> searchService(Long criteria) throws Exception {
        try {
            String query = "SELECT u.sifraUsluge, u.nazivUsluge, u.opisUsluge, u.sifraPredmetaProdaje, "
                    + "pp.sifraPredmetaProdaje, pp.cena, pp.cenaSaPorezom, pp.id, ps.id, ps.oznaka, ps.vrednost "
                    + "FROM usluga u JOIN predmetProdaje pp on (u.sifraPredmetaProdaje = pp.sifraPredmetaProdaje) "
                    + "JOIN poreskaStopa ps on (pp.id = ps.id) WHERE u.sifraPredmetaProdaje = ?"
                    + " OR u.sifraUsluge = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, criteria);
            statement.setLong(2, criteria);
            ResultSet rs = statement.executeQuery();
            List<DomainObject> services = new ArrayList<>();

            while (rs.next()) {
                PoreskaStopa poreskaStopa = new PoreskaStopa();
                poreskaStopa.setId(rs.getLong("ps.id"));
                poreskaStopa.setVrednost(rs.getBigDecimal("ps.vrednost"));
                poreskaStopa.setOznaka(rs.getString("ps.oznaka"));

                PredmetProdaje predmetProdaje = new PredmetProdaje();
                predmetProdaje.setSifraPredmetaProdaje(rs.getLong("pp.sifraPredmetaProdaje"));
                predmetProdaje.setCena(rs.getBigDecimal("pp.cena"));
                predmetProdaje.setCenaSaPorezom(rs.getBigDecimal("pp.cenaSaPorezom"));
                predmetProdaje.setPoreskaStopa(poreskaStopa);

                Usluga usluga = new Usluga();
                usluga.setPredmetProdaje(predmetProdaje);
                usluga.setNazivUsluge(rs.getString("u.nazivUsluge"));
                usluga.setOpisUsluge(rs.getString("u.opisUsluge"));
                usluga.setSifraUsluge(rs.getLong("u.sifraUsluge"));

                services.add(usluga);
            }

            return services;

        } catch (SQLException e) {
            throw new Exception(resourceBundle.getString("database_select_exception") + " usluga");
        }
    }

    /**
     * Method for searching objects of sale in a database.
     *
     * @param criteria is represents criteria for search.
     * @return list of object of sale.
     * @throws Exception if there are database connection problems.
     */
    public synchronized DomainObject searchObjectOfSale(Long criteria) throws Exception {
        try {
            String query = "SELECT pp.sifraPredmetaProdaje as 'pp_sifra', pp.cena as 'pp_cena', "
                    + "pp.cenaSaPorezom as 'pp_cenaSaP', pp.id as 'pp_id', ps.id as 'ps_id', ps.oznaka "
                    + "as 'ps_oznaka', ps.vrednost as 'ps_vrednost' FROM predmetProdaje pp JOIN"
                    + " poreskaStopa ps on (pp.id = ps.id) WHERE pp.sifraPredmetaProdaje = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, criteria);
            ResultSet rs = statement.executeQuery();
            PredmetProdaje predmetProdaje = new PredmetProdaje();

            while (rs.next()) {
                predmetProdaje.setSifraPredmetaProdaje(rs.getLong("pp_sifra"));
                predmetProdaje.setCena(rs.getBigDecimal("pp_cena"));
                predmetProdaje.setCenaSaPorezom(rs.getBigDecimal("pp_cenaSaP"));
                PoreskaStopa poreskaStopa = new PoreskaStopa();
                poreskaStopa.setId(rs.getLong("ps_id"));
                poreskaStopa.setVrednost(rs.getBigDecimal("ps_vrednost"));
                poreskaStopa.setOznaka(rs.getString("ps_oznaka"));
                predmetProdaje.setPoreskaStopa(poreskaStopa);
            }

            return predmetProdaje;

        } catch (SQLException e) {
            throw new Exception(resourceBundle.getString("database_select_exception") + " predmetprodaje");
        }
    }

    /**
     * Method for getting all objects of sale with names from a database.
     *
     * @return a map with key-value par (objectOfSale, name).
     * @throws Exception if there are database connection problems.
     */
    public synchronized Map<Object, Object> getAllObjectOfSaleWithNames() throws Exception {
        try {
            String query = "SELECT p.sifraPredmetaProdaje AS 'ID', p.`cena` AS 'Cena', "
                    + "p.cenaSaPorezom AS 'Cena_sa_porezom', p.id AS 'Porez_ID', "
                    + "ps.oznaka AS 'Oznaka', ps.vrednost AS 'Vrednost', d.nazivDela AS 'Naziv' "
                    + "FROM Deo d JOIN predmetprodaje p ON (d.sifraPredmetaProdaje=p.sifraPredmetaProdaje) "
                    + "JOIN poreskastopa ps ON (p.id=ps.id) "
                    + "UNION SELECT p.sifraPredmetaProdaje, "
                    + "p.cena, p.cenaSaPorezom, p.id, ps.oznaka, ps.vrednost, u.nazivUsluge FROM "
                    + "Usluga u JOIN predmetprodaje p ON (u.sifraPredmetaProdaje=p.sifraPredmetaProdaje) "
                    + "JOIN poreskastopa ps ON (p.id=ps.id) "
                    + "ORDER BY ID";

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            Map<Object, Object> map = new HashMap<>();

            while (rs.next()) {
                PoreskaStopa poreskaStopa = new PoreskaStopa();
                poreskaStopa.setId(rs.getLong("Porez_ID"));
                poreskaStopa.setVrednost(rs.getBigDecimal("Vrednost"));
                poreskaStopa.setOznaka(rs.getString("Oznaka"));

                PredmetProdaje predmetProdaje = new PredmetProdaje();
                predmetProdaje.setSifraPredmetaProdaje(rs.getLong("ID"));
                predmetProdaje.setCena(rs.getBigDecimal("Cena"));
                predmetProdaje.setCenaSaPorezom(rs.getBigDecimal("Cena_sa_porezom"));
                predmetProdaje.setPoreskaStopa(poreskaStopa);

                map.put(predmetProdaje, rs.getString("Naziv"));
            }

            return map;
        } catch (SQLException e) {
            throw new Exception(resourceBundle.getString("database_select_exception") + " predmetprodaje");
        }
    }

    /**
     * Method for searching objects of sale with names in a database.
     *
     * @param criteria is represents criteria for search.
     * @return a map with key-value par (objectOfSale, name).
     * @throws Exception if there are database connection problems.
     */
    public synchronized Map<Object, Object> searchObjectOfSaleWithNames(String criteria) throws Exception {
        try {
            String query = "SELECT p.sifraPredmetaProdaje AS 'ID', p.`cena` AS 'Cena', "
                    + "p.cenaSaPorezom AS 'Cena_sa_porezom', p.id AS 'Porez_ID', "
                    + "ps.oznaka AS 'Oznaka', ps.vrednost AS 'Vrednost', d.nazivDela AS 'Naziv' "
                    + "FROM Deo d JOIN predmetprodaje p ON (d.sifraPredmetaProdaje=p.sifraPredmetaProdaje) "
                    + "JOIN poreskastopa ps ON (p.id=ps.id) "
                    + "WHERE d.nazivDela LIKE '%?%' OR "
                    + "d.proizvodjac LIKE '%?%'"
                    + "UNION SELECT p.sifraPredmetaProdaje, "
                    + "p.cena, p.cenaSaPorezom, p.id, ps.oznaka, ps.vrednost, u.nazivUsluge FROM "
                    + "Usluga u JOIN predmetprodaje p ON (u.sifraPredmetaProdaje=p.sifraPredmetaProdaje) "
                    + "JOIN poreskastopa ps ON (p.id=ps.id) "
                    + "WHERE u.nazivUsluge LIKE '%?%'"
                    + "ORDER BY ID";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, criteria);
            statement.setString(2, criteria);
            statement.setString(3, criteria);
            ResultSet rs = statement.executeQuery();
            Map<Object, Object> map = new HashMap<>();

            while (rs.next()) {
                PoreskaStopa poreskaStopa = new PoreskaStopa();
                poreskaStopa.setId(rs.getLong("Porez_ID"));
                poreskaStopa.setVrednost(rs.getBigDecimal("Vrednost"));
                poreskaStopa.setOznaka(rs.getString("Oznaka"));

                PredmetProdaje predmetProdaje = new PredmetProdaje();
                predmetProdaje.setSifraPredmetaProdaje(rs.getLong("ID"));
                predmetProdaje.setCena(rs.getBigDecimal("Cena"));
                predmetProdaje.setCenaSaPorezom(rs.getBigDecimal("Cena_sa_porezom"));
                predmetProdaje.setPoreskaStopa(poreskaStopa);

                map.put(predmetProdaje, rs.getString("Naziv"));
            }

            return map;
        } catch (SQLException e) {
            throw new Exception(resourceBundle.getString("database_select_exception") + " predmetprodaje");
        }
    }

    /**
     * Method for inserting list of objects from a database.
     *
     * @param listOdo is represents list of object that user need to insert.
     * @throws Exception if there are database connection problems.
     */
    public void insertListOfDomainObject(List<DomainObject> listOdo) throws Exception {
        try {
            for (DomainObject domainObject : listOdo) {
                String query = "INSERT INTO " + domainObject.getTableName()
                        + " (" + domainObject.getAttributeNamesForInsert() + ") VALUES "
                        + "(" + domainObject.getAttributeValuesForInsert() + ")";

                Statement statement = connection.createStatement();
                statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
                ResultSet rs = statement.getGeneratedKeys();

                if (domainObject.isAutoincrement()) {
                    Long id = null;
                    while (rs.next()) {
                        id = rs.getLong(1);
                    }
                    domainObject.setObjectId(id);
                }
            }
        } catch (SQLException e) {
            throw new Exception(resourceBundle.getString("database_insert_exception") + " "
                    + listOdo.get(0).getTableName());
        }
    }

    /**
     * Method for searching bills in a database.
     *
     * @param criteria is represents criteria for search.
     * @return list of bills.
     * @throws Exception if there are database connection problems.
     */
    public List<DomainObject> searchBill(Long criteria) throws Exception {
        try {
            String query = "SELECT r.brojRacuna, r.datumIzdavanja, r.ukupnaVrednost,"
                    + " r.ukupnaVrednostSaPorezom, r.obradjen, r.storniran, ra.sifraRadnika, "
                    + "ra.imeRadnika, ra.PrezimeRadnika, ra.adresa, ra.telefon, ra.JMBG, "
                    + "ra.administrator, ra.username, ra.password, k.sifraKlijenta, k.imeKlijenta, "
                    + "k.prezimeKlijenta, k.brojPoseta, k.dug FROM racun r JOIN radnik ra ON "
                    + "(r.sifraRadnika = ra.sifraRadnika) JOIN klijent k ON (r.sifraKlijenta = k.sifraKlijenta)"
                    + "WHERE r.brojRacuna = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, criteria);
            ResultSet rs = statement.executeQuery();
            List<DomainObject> bills = new ArrayList<>();

            while (rs.next()) {
                Klijent klijent = new Klijent();
                klijent.setSifraKlijenta(rs.getLong("k.sifraKlijenta"));
                klijent.setImeKlijenta(rs.getString("k.imeKlijenta"));
                klijent.setPrezimeKlijenta(rs.getString("k.prezimeKlijenta"));
                klijent.setBrojPoseta(rs.getInt("k.brojPoseta"));
                klijent.setDug(rs.getBigDecimal("k.dug"));

                Radnik radnik = new Radnik();
                radnik.setSifraRadnika(rs.getLong("ra.sifraRadnika"));
                radnik.setImeRadnika(rs.getString("ra.imeRadnika"));
                radnik.setPrezimeRadnika(rs.getString("ra.prezimeRadnika"));
                radnik.setAdresa(rs.getString("ra.adresa"));
                radnik.setTelefon(rs.getString("ra.telefon"));
                radnik.setJMBG(rs.getString("ra.JMBG"));
                radnik.setAdministrator(rs.getBoolean("ra.administrator"));
                radnik.setUsername(rs.getString("ra.username"));
                radnik.setPassword(rs.getString("ra.password"));

                Racun racun = new Racun();
                racun.setBrojRacuna(rs.getLong("r.brojRacuna"));
                racun.setDatumIzdavanja(new java.util.Date(rs.getDate("r.datumIzdavanja").getTime()));
                racun.setUkupnaVrednost(rs.getBigDecimal("r.ukupnaVrednost"));
                racun.setUkupnaVrednostSaPorezom(rs.getBigDecimal("r.ukupnaVrednostSaPorezom"));
                racun.setObradjen(rs.getBoolean("r.obradjen"));
                racun.setStorniran(rs.getBoolean("r.storniran"));
                racun.setKlijent(klijent);
                racun.setRadnik(radnik);

                bills.add(racun);
            }

            return bills;
        } catch (SQLException ex) {
            throw new Exception(resourceBundle.getString("database_select_exception") + " racun");
        }
    }

    /**
     * Method for searching bills from date in a database.
     *
     * @param date is represents date criteria for search.
     * @return list of bills.
     * @throws Exception if there are database connection problems.
     */
    public List<DomainObject> searchBillFromDate(java.util.Date date) throws Exception {
        try {
            String query = "SELECT r.brojRacuna, r.datumIzdavanja, r.ukupnaVrednost,"
                    + " r.ukupnaVrednostSaPorezom, r.obradjen, r.storniran, ra.sifraRadnika, "
                    + "ra.imeRadnika, ra.PrezimeRadnika, ra.adresa, ra.telefon, ra.JMBG, "
                    + "ra.administrator, ra.username, ra.password, k.sifraKlijenta, k.imeKlijenta, "
                    + "k.prezimeKlijenta, k.brojPoseta, k.dug FROM racun r JOIN radnik ra ON "
                    + "(r.sifraRadnika = ra.sifraRadnika) JOIN klijent k ON (r.sifraKlijenta = k.sifraKlijenta)"
                    + "WHERE r.datumIzdavanja >= ? ORDER BY r.brojRacuna";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, new Date(date.getTime()));
            ResultSet rs = statement.executeQuery();
            List<DomainObject> bills = new ArrayList<>();

            while (rs.next()) {
                Klijent klijent = new Klijent();
                klijent.setSifraKlijenta(rs.getLong("k.sifraKlijenta"));
                klijent.setImeKlijenta(rs.getString("k.imeKlijenta"));
                klijent.setPrezimeKlijenta(rs.getString("k.prezimeKlijenta"));
                klijent.setBrojPoseta(rs.getInt("k.brojPoseta"));
                klijent.setDug(rs.getBigDecimal("k.dug"));

                Radnik radnik = new Radnik();
                radnik.setSifraRadnika(rs.getLong("ra.sifraRadnika"));
                radnik.setImeRadnika(rs.getString("ra.imeRadnika"));
                radnik.setPrezimeRadnika(rs.getString("ra.prezimeRadnika"));
                radnik.setAdresa(rs.getString("ra.adresa"));
                radnik.setTelefon(rs.getString("ra.telefon"));
                radnik.setJMBG(rs.getString("ra.JMBG"));
                radnik.setAdministrator(rs.getBoolean("ra.administrator"));
                radnik.setUsername(rs.getString("ra.username"));
                radnik.setPassword(rs.getString("ra.password"));

                Racun racun = new Racun();
                racun.setBrojRacuna(rs.getLong("r.brojRacuna"));
                racun.setDatumIzdavanja(new java.util.Date(rs.getDate("r.datumIzdavanja").getTime()));
                racun.setUkupnaVrednost(rs.getBigDecimal("r.ukupnaVrednost"));
                racun.setUkupnaVrednostSaPorezom(rs.getBigDecimal("r.ukupnaVrednostSaPorezom"));
                racun.setObradjen(rs.getBoolean("r.obradjen"));
                racun.setStorniran(rs.getBoolean("r.storniran"));
                racun.setKlijent(klijent);
                racun.setRadnik(radnik);

                bills.add(racun);
            }

            return bills;
        } catch (SQLException ex) {
            throw new Exception(resourceBundle.getString("database_select_exception") + " racun");
        }
    }

    /**
     * Method for searching new clients from date in a database.
     *
     * @param date is represents date criteria for search.
     * @return list of clients.
     * @throws Exception if there are database connection problems.
     */
    public List<DomainObject> searchNewClientsFromDate(java.util.Date date) throws Exception {
        try {
            String query = "SELECT k.sifraKlijenta, k.imeKlijenta, k.prezimeKlijenta, k.brojPoseta, "
                    + "k.dug FROM racun r JOIN klijent k ON (r.sifraKlijenta = k.sifraKlijenta) WHERE "
                    + "r.datumIzdavanja >= ? AND k.brojPoseta = 1 ORDER "
                    + "BY k.sifraKlijenta";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setDate(1, new Date(date.getTime()));
            ResultSet rs = statement.executeQuery();
            List<DomainObject> clients = new ArrayList<>();

            while (rs.next()) {
                Klijent klijent = new Klijent();
                klijent.setSifraKlijenta(rs.getLong("k.sifraKlijenta"));
                klijent.setImeKlijenta(rs.getString("k.imeKlijenta"));
                klijent.setPrezimeKlijenta(rs.getString("k.prezimeKlijenta"));
                klijent.setBrojPoseta(rs.getInt("k.brojPoseta"));
                klijent.setDug(rs.getBigDecimal("k.dug"));

                clients.add(klijent);
            }

            return clients;
        } catch (SQLException ex) {
            throw new Exception(resourceBundle.getString("database_select_exception") + " klijent");
        }
    }

    /**
     * Method for searching clients with debt in a database.
     *
     * @return list of clients.
     * @throws Exception if there are database connection problems.
     */
    public List<DomainObject> searchClientsWithDebt() throws Exception {
        try {
            String query = "SELECT k.sifraKlijenta, k.imeKlijenta, k.prezimeKlijenta, k.brojPoseta, "
                    + "k.dug FROM klijent k  WHERE "
                    + "k.dug > 0 ORDER BY k.dug DESC";

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
            List<DomainObject> clients = new ArrayList<>();

            while (rs.next()) {
                Klijent klijent = new Klijent();
                klijent.setSifraKlijenta(rs.getLong("k.sifraKlijenta"));
                klijent.setImeKlijenta(rs.getString("k.imeKlijenta"));
                klijent.setPrezimeKlijenta(rs.getString("k.prezimeKlijenta"));
                klijent.setBrojPoseta(rs.getInt("k.brojPoseta"));
                klijent.setDug(rs.getBigDecimal("k.dug"));

                clients.add(klijent);
            }

            return clients;
        } catch (SQLException ex) {
            throw new Exception(resourceBundle.getString("database_select_exception") + " klijent");
        }
    }

    /**
     * Method for searching tax rates in a database.
     *
     * @param id is represents criteria for search.
     * @return list of tax rates.
     * @throws Exception if there are database connection problems.
     */
    public List<DomainObject> searchTax(Long id) throws Exception {
        try {
            String query = "SELECT id, oznaka, vrednost FROM poreskaStopa "
                    + "WHERE id = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, id);
            ResultSet rs = statement.executeQuery();
            List<DomainObject> tax = new ArrayList<>();

            while (rs.next()) {
                PoreskaStopa poreskaStopa = new PoreskaStopa();
                poreskaStopa.setId(rs.getLong("id"));
                poreskaStopa.setOznaka(rs.getString("oznaka"));
                poreskaStopa.setVrednost(rs.getBigDecimal("vrednost"));

                tax.add(poreskaStopa);
            }

            return tax;
        } catch (SQLException ex) {
            throw new Exception(resourceBundle.getString("database_select_exception") + " poreskastopa");
        }
    }

    /**
     * Method for searching user in a database by specific username.
     *
     * @param username is represents criteria for search.
     * @return object of Klijent class.
     * @throws Exception if there are database connection problems.
     */
    public synchronized DomainObject checkUsername(String username) throws Exception {
        try {
            String query = "SELECT sifraRadnika, imeRadnika, prezimeRadnika, adresa, telefon, JMBG, administrator, "
                    + "username, password FROM radnik WHERE username = ?";
            PreparedStatement statement = connection.prepareStatement(query);

            statement.setString(1, username);
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
        } catch (SQLException ex) {
            throw new Exception(resourceBundle.getString("database_select_exception") + " radnik");
        }
    }

    /**
     * Method for searching bills in a database.
     *
     * @param customerId is represents id of customer for bill search.
     * @return list of bills.
     * @throws Exception if there are database connection problems.
     */
    public List<DomainObject> searchBillByCustomerId(Long customerId) throws Exception {
        try {
            String query = "SELECT r.brojRacuna, r.datumIzdavanja, r.ukupnaVrednost,"
                    + " r.ukupnaVrednostSaPorezom, r.obradjen, r.storniran, ra.sifraRadnika, "
                    + "ra.imeRadnika, ra.PrezimeRadnika, ra.adresa, ra.telefon, ra.JMBG, "
                    + "ra.administrator, ra.username, ra.password, k.sifraKlijenta, k.imeKlijenta, "
                    + "k.prezimeKlijenta, k.brojPoseta, k.dug FROM racun r JOIN radnik ra ON "
                    + "(r.sifraRadnika = ra.sifraRadnika) JOIN klijent k ON (r.sifraKlijenta = k.sifraKlijenta)"
                    + "WHERE k.sifraKlijenta = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setLong(1, customerId);
            ResultSet rs = statement.executeQuery();
            List<DomainObject> bills = new ArrayList<>();

            while (rs.next()) {
                Klijent klijent = new Klijent();
                klijent.setSifraKlijenta(rs.getLong("k.sifraKlijenta"));
                klijent.setImeKlijenta(rs.getString("k.imeKlijenta"));
                klijent.setPrezimeKlijenta(rs.getString("k.prezimeKlijenta"));
                klijent.setBrojPoseta(rs.getInt("k.brojPoseta"));
                klijent.setDug(rs.getBigDecimal("k.dug"));

                Radnik radnik = new Radnik();
                radnik.setSifraRadnika(rs.getLong("ra.sifraRadnika"));
                radnik.setImeRadnika(rs.getString("ra.imeRadnika"));
                radnik.setPrezimeRadnika(rs.getString("ra.prezimeRadnika"));
                radnik.setAdresa(rs.getString("ra.adresa"));
                radnik.setTelefon(rs.getString("ra.telefon"));
                radnik.setJMBG(rs.getString("ra.JMBG"));
                radnik.setAdministrator(rs.getBoolean("ra.administrator"));
                radnik.setUsername(rs.getString("ra.username"));
                radnik.setPassword(rs.getString("ra.password"));

                Racun racun = new Racun();
                racun.setBrojRacuna(rs.getLong("r.brojRacuna"));
                racun.setDatumIzdavanja(new java.util.Date(rs.getDate("r.datumIzdavanja").getTime()));
                racun.setUkupnaVrednost(rs.getBigDecimal("r.ukupnaVrednost"));
                racun.setUkupnaVrednostSaPorezom(rs.getBigDecimal("r.ukupnaVrednostSaPorezom"));
                racun.setObradjen(rs.getBoolean("r.obradjen"));
                racun.setStorniran(rs.getBoolean("r.storniran"));
                racun.setKlijent(klijent);
                racun.setRadnik(radnik);

                bills.add(racun);
            }

            return bills;
        } catch (SQLException ex) {
            throw new Exception(resourceBundle.getString("database_select_exception") + " racun");
        }
    }
}
