package factory;

import impl.ObservableImpl;

/**
 * Created by kahlil on 3/22/15.
 */
public class ObservableImplFactory {

    public static ObservableImpl create(String tckr, String obsIn) {
        return new ObservableImpl(tckr, obsIn);
    }
}
