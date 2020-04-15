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
import java.util.logging.Level;
import java.util.logging.Logger;

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
            throw new SQLException(languageBundle.getString("connectionProblem"));
        } catch (FileNotFoundException e) {
            throw new FileNotFoundException(languageBundle.getString("problemsWithFile"));
        } catch (ClassNotFoundException e) {
            throw new ClassNotFoundException(languageBundle.getString("classNotFound"));
        } catch (IOException e) {
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

    public synchronized DomainObject updateDomainObject(DomainObject odo) throws Exception {
        try {
            String query = "UPDATE " + odo.getTableName() + " SET "
                    + odo.getAttributesForUpdate() + " WHERE " + odo.getIdentifierName()
                    + " = " + odo.getObjectId();

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
            String query = "SELECT * FROM klijent WHERE imeKlijenta LIKE '%" + criteria
                    + "%' OR prezimeKlijenta LIKE '%" + criteria + "%' OR sifraKlijenta = '" + criteria + "'";

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

    public List<DomainObject> searchEmployees(String criteria) throws Exception {
        try {
            String query = "SELECT * FROM radnik WHERE imeRadnika LIKE '%" + criteria
                    + "%' OR prezimeRadnika LIKE '%" + criteria + "%' OR sifraRadnika = '" + criteria + "'";

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
            e.printStackTrace();
            throw new Exception();
        }
    }

    public synchronized DomainObject deleteDomainObject(DomainObject odo) throws Exception {
        try {
            String query = "DELETE FROM " + odo.getTableName() + " WHERE "
                    + odo.getIdentifierName() + " = " + odo.getObjectId();

            Statement statement = connection.createStatement();
            statement.executeUpdate(query);

            return odo;

        } catch (SQLException e) {
            e.printStackTrace();
            throw new Exception();
        }
    }

    public DomainObject insertDomainObject(DomainObject odo) throws Exception {
        try {
            String query = "INSERT INTO " + odo.getTableName() + " ("
                    + odo.getAttributeNamesForInsert() + ") VALUES (" + odo.getAttributeValuesForInsert() + ")";

            Statement statement = connection.createStatement();
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
            e.printStackTrace();
            throw new Exception();
        }
    }

    public List<DomainObject> selectAllTax() throws Exception {
        try {
            String query = "SELECT * FROM poreskastopa";

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
            e.printStackTrace();
            throw new Exception();
        }
    }

    public List<DomainObject> searchCarPart(String criteria) throws Exception {
        try {
            String query = "SELECT d.serijskiBroj, d.nazivDela, d.proizvodjac, "
                    + "d.opis, d.sifraPredmetaProdaje, d.stanje, pp.sifraPredmetaProdaje, "
                    + "pp.cena, pp.cenaSaPorezom, pp.id, ps.id, ps.oznaka, ps.vrednost "
                    + "FROM deo d JOIN predmetProdaje pp on (d.sifraPredmetaProdaje = pp.sifraPredmetaProdaje) "
                    + "JOIN poreskaStopa ps on (pp.id = ps.id) WHERE d.nazivDela LIKE '%"
                    + criteria + "%' OR d.serijskiBroj = '" + criteria + "'";

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
            e.printStackTrace();
            throw new Exception();
        }
    }

    public List<DomainObject> searchService(String criteria) throws Exception {
        try {
            String query = "SELECT u.sifraUsluge, u.nazivUsluge, u.opisUsluge, u.sifraPredmetaProdaje, pp.sifraPredmetaProdaje, "
                    + "pp.cena, pp.cenaSaPorezom, pp.id, ps.id, ps.oznaka, ps.vrednost "
                    + "FROM usluga u JOIN predmetProdaje pp on (u.sifraPredmetaProdaje = pp.sifraPredmetaProdaje) "
                    + "JOIN poreskaStopa ps on (pp.id = ps.id) WHERE u.nazivUsluge LIKE '%" + criteria
                    + "%' OR u.sifraUsluge = '" + criteria + "'";

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
            e.printStackTrace();
            throw new Exception();
        }
    }

    public synchronized DomainObject searchObjectOfSale(Long criteria) throws Exception {
        try {
            String query = "SELECT pp.sifraPredmetaProdaje as 'pp_sifra', pp.cena as 'pp_cena', pp.cenaSaPorezom as 'pp_cenaSaP', "
                    + "pp.id as 'pp_id', ps.id as 'ps_id', ps.oznaka as 'ps_oznaka', ps.vrednost as 'ps_vrednost' FROM predmetProdaje pp JOIN"
                    + " poreskaStopa ps on (pp.id = ps.id) WHERE pp.sifraPredmetaProdaje = " + criteria;

            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery(query);
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
            e.printStackTrace();
            throw new Exception();
        }
    }

    public synchronized Map<Object, Object> getAllObjectOfSaleWithNames(String criteria) throws Exception {
        try {
            String query = "SELECT p.sifraPredmetaProdaje AS 'ID', p.`cena` AS 'Cena', "
                    + "p.cenaSaPorezom AS 'Cena_sa_porezom', p.id AS 'Porez_ID', "
                    + "ps.oznaka AS 'Oznaka', ps.vrednost AS 'Vrednost', d.nazivDela AS 'Naziv' "
                    + "FROM Deo d JOIN predmetprodaje p ON (d.sifraPredmetaProdaje=p.sifraPredmetaProdaje) "
                    + "JOIN poreskastopa ps ON (p.id=ps.id) "
                    + "WHERE d.nazivDela LIKE '%" + criteria + "%' OR "
                    + "d.proizvodjac LIKE '%" + criteria + "%'"
                    + "UNION SELECT p.sifraPredmetaProdaje, "
                    + "p.cena, p.cenaSaPorezom, p.id, ps.oznaka, ps.vrednost, u.nazivUsluge FROM "
                    + "Usluga u JOIN predmetprodaje p ON (u.sifraPredmetaProdaje=p.sifraPredmetaProdaje) "
                    + "JOIN poreskastopa ps ON (p.id=ps.id) "
                    + "WHERE u.nazivUsluge LIKE '%" + criteria + "%'"
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
            e.printStackTrace();
            throw new Exception();
        }
    }

    public void insertListOfDomainObject(List<DomainObject> listOdo) throws Exception {
        try {
            for (DomainObject domainObject : listOdo) {
                String query = "INSERT INTO " + domainObject.getTableName() + " ("
                        + domainObject.getAttributeNamesForInsert() + ") VALUES (" + domainObject.getAttributeValuesForInsert() + ")";

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
            e.printStackTrace();
            throw new Exception();
        }
    }

    public List<DomainObject> searchBill(String criteria) throws Exception {
        try {
            String query = "SELECT r.brojRacuna, r.datumIzdavanja, r.ukupnaVrednost,"
                    + " r.ukupnaVrednostSaPorezom, r.obradjen, r.storniran, ra.sifraRadnika, "
                    + "ra.imeRadnika, ra.PrezimeRadnika, ra.adresa, ra.telefon, ra.JMBG, "
                    + "ra.administrator, ra.username, ra.password, k.sifraKlijenta, k.imeKlijenta, "
                    + "k.prezimeKlijenta, k.brojPoseta, k.dug FROM racun r JOIN radnik ra ON "
                    + "(r.sifraRadnika = ra.sifraRadnika) JOIN klijent k ON (r.sifraKlijenta = k.sifraKlijenta)"
                    + "WHERE r.brojRacuna = " + criteria + " OR k.sifraKlijenta = " + criteria;

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
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception();
        }
    }

    public List<DomainObject> searchBillFromDate(java.util.Date date) throws Exception {
        try {
            String query = "SELECT r.brojRacuna, r.datumIzdavanja, r.ukupnaVrednost,"
                    + " r.ukupnaVrednostSaPorezom, r.obradjen, r.storniran, ra.sifraRadnika, "
                    + "ra.imeRadnika, ra.PrezimeRadnika, ra.adresa, ra.telefon, ra.JMBG, "
                    + "ra.administrator, ra.username, ra.password, k.sifraKlijenta, k.imeKlijenta, "
                    + "k.prezimeKlijenta, k.brojPoseta, k.dug FROM racun r JOIN radnik ra ON "
                    + "(r.sifraRadnika = ra.sifraRadnika) JOIN klijent k ON (r.sifraKlijenta = k.sifraKlijenta)"
                    + "WHERE r.datumIzdavanja >= " + new Date(date.getTime())
                    + " ORDER BY r.brojRacuna";

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
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception();
        }
    }

    public List<DomainObject> searchNewClientsFromDate(java.util.Date date) throws Exception {
        try {
            String query = "SELECT k.sifraKlijenta, k.imeKlijenta, k.prezimeKlijenta, k.brojPoseta, "
                    + "k.dug FROM racun r JOIN klijent k ON (r.sifraKlijenta = k.sifraKlijenta) WHERE "
                    + "r.datumIzdavanja >=" + new Date(date.getTime()) + " AND k.`brojPoseta` = 1 ORDER "
                    + "BY k.sifraKlijenta";

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
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception();
        }
    }
    
    public List<DomainObject> searchClientsWithDebt() throws Exception {
        try {
            String query = "SELECT k.sifraKlijenta, k.imeKlijenta, k.prezimeKlijenta, k.brojPoseta, "
                    + "k.dug FROM racun r JOIN klijent k ON (r.sifraKlijenta = k.sifraKlijenta) WHERE "
                    + "k.dug > 1 ORDER BY k.dug DESC";

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
        } catch (Exception ex) {
            ex.printStackTrace();
            throw new Exception();
        }
    }
}
