package services;

import factory.ObservableImplFactory;
import impl.ObservableImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.util.concurrent.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by kahlil on 3/21/15.
 */
public class UrlOpener extends Thread {

    private static final Logger log = Logger.getLogger(UrlOpener.class.getName());
    private static ConcurrentHashMap<LocalDate, ObservableImpl> chm;
    private static CopyOnWriteArrayList<LocalDate> keys;
    private static ExecutorService es;
    private static String line;
    private static int maxThreads;
    private static String ticker;
    private static URL url;
    private static HttpURLConnection urlConnection;

    public UrlOpener(String stock, URL urlIn) {
        chm = new ConcurrentHashMap();
        keys = new CopyOnWriteArrayList<LocalDate>();
        line = "";
        ticker = stock;
        url = urlIn;
        maxThreads = 2;
        es = Executors.newFixedThreadPool(maxThreads);
        System.out.println("Received " + url);
    }

    public void run() {
        try {
            log.log(Level.INFO, "Opening HTTP connection to " + url);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(5 * 1000);
            urlConnection.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            while ((line = br.readLine()) != null) {
                getLine(line);
            }

            urlConnection.disconnect();
            br.close();
            es.shutdown();
            //getKeys();

        } catch (IOException ioe) { ioe.printStackTrace(); }
    }

    private static void getKeys() {
        for (LocalDate ld : keys) {
            ObservableImpl o = null;
            System.out.print(ld + " ");
            o = chm.get(ld);
            System.out.println(o.getClose());
        }
    }

    private static void getLine(String lineIn) {
        String[] lineInSplit = lineIn.split(",");
        if ((lineInSplit.length == 6) && (!lineInSplit[0].matches("values:Date"))) {
            setObservation(lineIn);
        }
    }

    private static void setObservation(String lineIn) {
        es.execute(ObservableImplFactory.create(ticker, lineIn));
    }
}
