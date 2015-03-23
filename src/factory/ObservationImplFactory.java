package factory;

import impl.ObservationImpl;
import interfaces.Observation;

/**
 * Created by kahlil on 3/22/15.
 */
public class ObservationImplFactory {

    public static Observation create(String obsIn) {
        return new ObservationImpl(obsIn);
    }
}
