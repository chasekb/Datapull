import services.UrlOpener;

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
    private static String urlPre;
    private static String urlPost;
    private static URL urlToVisit;

    public static void main(String[] args) throws IOException {
        stock = "AAPL";
        setUrlToVisit(stock);
        new UrlOpener(stock, getUrlToVisit()).start();

        /*
        try {
            DatabaseMetaData md = ConnectableImplFactory.create().makeConnection().getMetaData();
            System.out.println(md.getDatabaseProductVersion());
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }

    public static void setUrlToVisit(String stockIn) {
        urlPre = "http://chartapi.finance.yahoo.com/instrument/1.0/";
        urlPost = "/chartdata;type=quote;range=1y/csv";

        StringBuilder sb = new StringBuilder();
        sb.append(urlPre);
        sb.append(stockIn);
        sb.append(urlPost);

        try {
            urlToVisit = new URL(sb.toString());
        } catch (MalformedURLException mue) { mue.printStackTrace(); }
    }

    public static URL getUrlToVisit() {
        return urlToVisit;
    }
}
