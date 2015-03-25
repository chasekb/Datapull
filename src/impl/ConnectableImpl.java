package impl;

import interfaces.Connectable;
import oracle.jdbc.pool.OracleDataSource;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by kahlil on 3/24/15.
 */
public class ConnectableImpl implements Connectable {

    private static final Logger log = Logger.getLogger(ConnectableImpl.class.getName());
    private Connection c;
    private OracleDataSource ods;

    public ConnectableImpl() {

    }

    /**
     * Establish connection to database.
     */
    public Connection makeConnection() {
        log.log(Level.INFO, "Establishing database connection...");

        try {
            ods = new OracleDataSource();
            ods.setDriverType("oracle.jdbc.driver.OracleDriver");
            ods.setURL("jdbc:oracle:thin:server:1521:def");
            ods.setUser("id");
            ods.setPassword("password");
            ods.setDatabaseName("TEST");

            c = ods.getConnection();
        } catch (SQLException se) { se.printStackTrace(); }

        return c;
    }

    public void closeConnection(Connection c) {
        if (c != null) {
            try {
                c.close();
                log.log(Level.INFO, "Database connection closed.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Retrieves and outputs the tables in the database.
     */
    private void getDbMetaData() {
        try {
            DatabaseMetaData md = c.getMetaData();
            ResultSet mdrs = md.getTables(null, null, "%", null);
            while (mdrs.next()) {
                System.out.println(mdrs.getString(3));
            }

            int maxConnections = md.getMaxConnections();
            System.out.println(maxConnections);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
