import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Helper {
    public static Document getDoc(String url) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    public static <T> ArrayList<T> readFile(Class<T> clazz, String fileName) {
        ArrayList<T> list = new ArrayList<T>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(fileName)));

            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }
                list.add(clazz.getConstructor(String.class).newInstance(line));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void writeFile(String fileName, String line) {
        try {
            FileWriter writer = new FileWriter(new File(fileName), true);
            writer.write(line);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(String fileName, ArrayList<Job> jobs, String website) {
        try {
            FileWriter writer = new FileWriter(new File(fileName), true);
            if (fileName.contains("history")) {
                for (Job job : jobs) {
                    writer.write(job.toString());
                }
            } else {
                writer.write(website.substring(0, 1).toUpperCase() + website.substring(1) + " results\n\n");
                for (Job job : jobs) {
                    writer.write(job.getTitle() + "\n" + job.getCompany() + "\n" + job.getLocation() + "\n" +
                            job.getLink() + "\n\n");
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
