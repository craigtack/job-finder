package main;

import org.ini4j.Ini;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utility.FileIO;

import java.util.ArrayList;

public class Indeed extends Website {

    public Indeed() {
        name = "indeed";
        url = constructUrl();
        numOfPages = Integer.parseInt(Configuration.getConfig().get("indeed").get("pages"));
        pages = generatePages();
        history = FileIO.readFile(Job.class, "indeed-history.txt");
    }

    private String constructUrl() {
        Ini config = Configuration.getConfig();
        return "http://www.indeed.com/jobs?q=" +
                config.get("indeed").get("keyword").replace(',', '+') + "&l=" +
                config.get("indeed").get("city") + ",+" + config.get("indeed").get("state") + "&radius=" +
                config.get("indeed").get("radius") + "&start=";
    }

    private ArrayList<String> generatePages() {
        ArrayList<String> links = new ArrayList<String>();
        for (int i = 0; i < getNumOfPages(); i += 10) {
            links.add(getUrl() + String.valueOf(i));
        }
        return links;
    }

    public static ArrayList<Job> parseHTML(ArrayList<Document> scrapedPages) {
        ArrayList<Job> foundJobs = new ArrayList<Job>();

        for (Document doc : scrapedPages) {
            Elements containers = doc.getElementsByClass("row");
            for (Element container : containers) {
                foundJobs.add(new Job(container.child(0).text(), container.getElementsByClass("company").text(),
                        container.getElementsByClass("location").text(), "http://www.indeed.com" +
                        container.select("a").first().attr("href")));
            }
        }
        return foundJobs;
    }
}
