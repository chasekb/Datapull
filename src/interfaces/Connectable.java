package interfaces;

import java.sql.Connection;

/**
 * Created by kahlil on 3/24/15.
 */
public interface Connectable {
    Connection makeConnection();
    void closeConnection(Connection c);
}
