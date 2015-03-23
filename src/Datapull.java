import java.io.BufferedReader;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by kahlil on 3/20/15.
 */
public class Datapull {

    private static BufferedReader br;
    private static String stock;
    private static URL urlToVisit;

    public static void main(String[] args) throws IOException {
        setUrlToVisit("http://chartapi.finance.yahoo.com/instrument/1.0/AAPL/chartdata;type=quote;range=1y/csv");
        new UrlOpener(getUrlToVisit()).start();
    }

    public static void setUrlToVisit(String urlToVisitIn) {
        try {
            urlToVisit = new URL(urlToVisitIn);
        } catch (MalformedURLException mue) { mue.printStackTrace(); }
    }

    public static URL getUrlToVisit() {
        return urlToVisit;
    }
}
