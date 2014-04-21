package main;

import org.ini4j.Ini;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utility.FileIO;

import java.util.ArrayList;

public class Monster extends Website {

    public Monster() {
        name = "monster";
        url = constructURL();
        numOfPages = Integer.parseInt(Configuration.getConfig().get("monster").get("pages"));
        pages = generatePages();
        history = FileIO.readFile(Job.class, "monster-history.txt");
    }

    private String constructURL() {
        Ini config = Configuration.getConfig();
        String beginUrl = "http://jobsearch.monster.com/search/" +
                config.get("monster").get("state").replace(' ', '-') + "+Southern_12?q=" +
                config.get("monster").get("keyword").replace(',', '-') + "&pg=";
        String endUrl = "&where=" + config.get("monster").get("zip") + "&rad=" +
                config.get("monster").get("radius") + "-miles&sort=di.rv.dt";
        return beginUrl + endUrl;
    }

    private ArrayList<String> generatePages() {
        ArrayList<String> links = new ArrayList<String>();
        for (int i = 0; i < getNumOfPages(); i++) {
            links.add(getUrl().substring(0, getUrl().indexOf("&pg=") + 4) + String.valueOf(i + 1) +
                        getUrl().substring(getUrl().indexOf("&where"), getUrl().length()));
        }
        return links;
    }

    public static ArrayList<Job> parseHTML(ArrayList<Document> scrapedPages) {
        ArrayList<Job> foundJobs = new ArrayList<Job>();

        for (Document doc : scrapedPages) {
            Elements jobContainers = doc.getElementsByClass("jobTitleContainer").tagName("a");
            Elements companyContainers = doc.getElementsByClass("companyContainer").tagName("a");

            for (int i = 0; i < jobContainers.size(); i++) {
                Element jobContainer = jobContainers.get(i);
                Element companyContainer = companyContainers.get(i);
                foundJobs.add(new Job(jobContainer.child(0).text(), companyContainer.text().substring(9),
                        jobContainer.child(0).attr("data-m_impr_j_city"), jobContainer.child(0).attr("href")));
            }
        }
        return foundJobs;
    }
}
