package impl;

import interfaces.Observation;
import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by kahlil on 3/21/15.
 */
public class ObservationImpl implements Observation {
    private Connection c;
    private LocalDate ld;
    private ResultSet rs;
    private static final Logger log = Logger.getLogger(ObservationImpl.class.getName());
    private float close;
    private float high;
    private float low;
    private float open;
    private long volume;
    private String in;
    private int inLength;

    public ObservationImpl(String o) {
        in = o;
        inConvert();
        makeConnection();
        closeConnection();
    }

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

    private void closeConnection() {
        try {
            if (!c.isClosed()) {
                c.close();
            }
        } catch (SQLException se) { se.printStackTrace(); }

        log.log(Level.INFO, "Database connection closed.");
    }

    private void makeConnection() {
        String selectTableSQL1 = "INSERT INTO Observations VALUES ('" + getLd() + "', '" + getClose() + "', '" +
                getHigh() + "', '" + getLow() + "', '" + getOpen() + "')";

        log.log(Level.INFO, "Establishing database connection...");

        try {
            OracleDataSource ods = new OracleDataSource();
            ods.setDriverType("oracle.jdbc.driver.OracleDriver");
            ods.setURL("jdbc:oracle:thin:server:1521:def");
            ods.setUser("id");
            ods.setPassword("password");
            ods.setDatabaseName("TEST");

            c = ods.getConnection();
            java.sql.Statement s = c.createStatement();

            rs = s.executeQuery(selectTableSQL1);

            rs.close();
            s.close();
            c.close();
            log.log(Level.INFO, "processing entry " + getLd());

        } catch (SQLException se) { se.printStackTrace(); }
    }

    public LocalDate getLd() {
        return ld;
    }

    public float getClose() {
        return close;
    }

    public float getHigh() {
        return high;
    }

    public float getLow() {
        return low;
    }

    public float getOpen() {
        return open;
    }

    public long getVolume() { return volume; }
}
