package factory;

import impl.ConnectableImpl;
import interfaces.Connectable;

/**
 * Created by kahlil on 3/24/15.
 */
public class ConnectableImplFactory {

    public static Connectable create() {
        return new ConnectableImpl();
    }
}
