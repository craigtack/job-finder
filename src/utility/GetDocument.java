package utility;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import utility.ThreadSleep;

import java.io.IOException;
import java.net.SocketTimeoutException;

public class GetDocument {
    private GetDocument(){}

    public static Document get(final String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (SocketTimeoutException e) {
            sleep();
            get(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    private static void sleep() {
        ThreadSleep.sleep();
    }
}
