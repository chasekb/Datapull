package interfaces;

import java.time.LocalDate;

/**
 * Created by kahlil on 3/21/15.
 */
public interface Observable {
    LocalDate getLd();
    float getClose();
    float getHigh();
    float getLow();
    float getOpen();
    long getVolume();
}
