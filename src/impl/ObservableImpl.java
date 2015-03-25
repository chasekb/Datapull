package impl;

import factory.ConnectableImplFactory;
import interfaces.Connectable;
import interfaces.Observable;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by kahlil on 3/21/15.
 */
public class ObservableImpl implements Observable, Runnable {
    private Connection c;
    private LocalDate ld;
    private Connectable conn;
    private ResultSet mdrs = null;
    private ResultSet rs;
    private Statement s;
    private static final Logger log = Logger.getLogger(ObservableImpl.class.getName());
    private float close;
    private float high;
    private float low;
    private float open;
    private long volume;
    private String in;
    private String ticker;
    private int inLength;

    public ObservableImpl(String tckr, String o) {
        in = o;
        ticker = tckr;
        conn = ConnectableImplFactory.create();
        inConvert();
    }

    /**
     * Maps scraped observation to SQL fields.
     */
    private void inConvert() {
        String[] inSplit = in.split(",");
        inLength = inSplit.length;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");

        ld = LocalDate.parse(inSplit[0], formatter);
        close = Float.valueOf(inSplit[1]);
        high = Float.valueOf(inSplit[2]);
        low = Float.valueOf(inSplit[3]);
        open = Float.valueOf(inSplit[4]);
        volume = Long.valueOf(inSplit[5]);
    }

    public void run() {
        makeConnection();
        createTable();
        insertObservation();
        closeConnection();
    }

    /**
     * Establish connection to database.
     */
    private void makeConnection() {
        c = conn.makeConnection();

        try {
            s = c.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new table in the database for the ticker from which the current observation is drawn if no table
     * exists in the database for the ticker.
     */
    private void createTable() {
        String createTable =
                "CREATE TABLE " + ticker + "(\n" +
                        "Yearmonth   VARCHAR2(10),\n" +
                        "Close       DECIMAL(10,2) NOT NULL,\n" +
                        "Hi          DECIMAL(10,2) NOT NULL,\n" +
                        "Lo          DECIMAL(10,2) NOT NULL ,\n" +
                        "Open        DECIMAL(10,2) NOT NULL ,\n" +
                        "PRIMARY KEY (Yearmonth)\n)";

        try {
            DatabaseMetaData md = c.getMetaData();
            ResultSet mdrs = md.getTables(null, null, ticker, null);
            if (!mdrs.next()) {
                rs = s.executeQuery(createTable);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Insert current observation into database if not already in database.
     */
    private void insertObservation() {
        String checkForThis = "SELECT * FROM " + ticker + " WHERE Yearmonth = '" + getLd() + "'";
        String obsToIns =
                "INSERT INTO " + ticker + " VALUES ('" + getLd() + "', '" + getClose() + "', '" +
                        getHigh() + "', '" + getLow() + "', '" + getOpen() + "')";

        try {
            ResultSet cft = s.executeQuery(checkForThis);
            if (!cft.next()) {
                rs = s.executeQuery(obsToIns);
                log.log(Level.INFO, "processing entry " + getLd());
            }
        } catch (SQLException se) { se.printStackTrace(); }
    }

    /**
     * Closes connection to database if connection is not closed.
     */
    private void closeConnection() {
        try {
            if (rs != null) {
                rs.close();
            }

            if (s != null) {
                s.close();
            }

            if (c != null) {
                conn.closeConnection(c);
            }
        } catch (SQLException se) { se.printStackTrace(); }
    }

    /**
     * Returns this observation's date.
     * @return the date in in yyyy-MM-dd format
     */
    public LocalDate getLd() {
        return ld;
    }

    /**
     * Returns this observation's closing price.
     * @return the closing price
     */
    public float getClose() {
        return close;
    }

    /**
     * Returns this observation's high price.
     * @return the high price
     */
    public float getHigh() {
        return high;
    }

    /**
     * Returns this observation's low price.
     * @return the low price
     */
    public float getLow() {
        return low;
    }

    /**
     * Returns this observation's open price.
     * @return the open price
     */
    public float getOpen() {
        return open;
    }

    /**
     * Returns this observation's volume.
     * @return the volume
     */
    public long getVolume() { return volume; }
}
