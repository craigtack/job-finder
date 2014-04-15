import com.google.api.services.urlshortener.model.Url;
import org.ini4j.Ini;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class JobFinder {
    private ArrayList<Job> jobs;
    private ArrayList<Filter> filters;
    private ArrayList<Job> history;
    private Ini config;
    // "Constants"
    private static String DIR;
    private String MONSTER_BEGIN;
    private String MONSTER_END;
    private static int MONSTER_MAX;
    private String INDEED_URL;
    private static int INDEED_MAX;
    private int METHOD;
    private String EMAIL;
    private String PASSWORD;

    public JobFinder(String website) {
        config = readConfig();
        // "Constants"
        DIR = config.get("path").get("dir");
        MONSTER_BEGIN = "http://jobsearch.monster.com/search/" +
                config.get("monster").get("state").replace(' ', '-') + "+Southern_12?q=" +
                config.get("monster").get("keyword").replace(',', '-') + "&pg=";
        MONSTER_END = "&where=" + config.get("monster").get("zip") + "&rad=" +
                config.get("monster").get("radius") + "-miles&sort=di.rv.dt";
        MONSTER_MAX = Integer.parseInt(config.get("monster").get("pages"));
        INDEED_URL = "http://www.indeed.com/jobs?q=" +
                config.get("indeed").get("keyword").replace(',', '+') + "&l=" +
                config.get("indeed").get("city") + ",+" + config.get("indeed").get("state") + "&radius=" +
                config.get("indeed").get("radius") + "&start=";
        INDEED_MAX = Integer.parseInt(config.get("indeed").get("pages"));
        METHOD = Integer.parseInt(config.get("method").get("option"));
        EMAIL = config.get("gmail").get("email");
        PASSWORD = config.get("gmail").get("password");
        jobs = new ArrayList<Job>();
        filters = Helper.readFile(Filter.class, DIR + "filters.txt");

        if (website.equals("monster")) {
            history = Helper.readFile(Job.class, DIR + "monster-history.txt");
        } else if (website.equals("indeed")) {
            history = Helper.readFile(Job.class, DIR + "indeed-history.txt");
        }
    }

    public static void main(String[] args) {
        // Help
        if (args.length == 0) {
            displayHelp();
            return;
        }
        // Selection
        if (args[0].equals("both")) {
            new JobFinder("monster").start(MONSTER_MAX, 1, "monster");
            new JobFinder("indeed").start(INDEED_MAX * 10, 10, "indeed");
            return;
        } else if (args[0].equals("monster")) {
            new JobFinder("monster").start(MONSTER_MAX, 1, "monster");
            return;
        } else if (args[0].equals("indeed")) {
            new JobFinder("indeed").start(INDEED_MAX * 10, 10, "indeed");
            return;
        }

        // Add
        if (args[0].equals("addFilter")) {
            if (args.length == 1) {
                System.out.println("Error: addFilter requires additional parameter\n" +
                        "Try 'JobFinder help' for more information");
            } else {
                if (args[1].contains("--title=")) {
                    addFilter(args[1].substring(8, args[1].length() - 2), "title");
                } else if (args[1].contains("--company=")) {
                    addFilter(args[1].substring(10, args[1].length() - 2), "company");
                } else if (args[1].contains("--location=")) {
                    addFilter(args[1].substring(11, args[1].length() - 2), "location");
                }
            }
        }
    }

    /*
    CONFIG
     */
    public Ini readConfig() {
        Ini ini = null;
        try {
            ini = new Ini(new File("src\\config.ini"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ini;
    }

    /*
    START
     */
    public void start(int max, int increment, String website) {
        String url = null;
        for (int i = 0; i < max; i += increment) {
            if (website.equals("monster")) {
                url = MONSTER_BEGIN + String.valueOf(i + 1) + MONSTER_END;
            } else {
                url = INDEED_URL + String.valueOf(i);
            }
            parseHTML(Helper.getDoc(url, false), website);
        }
        if (!jobs.isEmpty()) {
            commit(website);
        }
    }

    private void parseHTML(Document doc, String website) {
        Job job = null;
        if (website.equals("monster")) {
            // Create map of job to company
            HashMap<Element, Element> jobInfo = getJobInfo(doc);
            for (Map.Entry entry : jobInfo.entrySet()) {
                Element jobLink = (Element) entry.getKey();
                Element companyLink = (Element) entry.getValue();
                job = new Job(jobLink.child(0).text(), companyLink.text().substring(9),
                        jobLink.child(0).attr("data-m_impr_j_city"), jobLink.child(0).attr("href"));
                if (checkJob(job)) {
                    jobs.add(job);
                }
            }
        } else {
            Elements containers = doc.getElementsByClass("row");
            //Url longURL = null;
            for (Element info : containers) {
                //longURL = new Url();
                //longURL.setLongUrl("http://www.indeed.com" + info.child(0).attr("href"));
                //String shortURL = longURL.
                job = new Job(info.child(0).text(), info.getElementsByClass("company").text(),
                        info.getElementsByClass("location").text(), "http://www.indeed.com" +
                        info.select("a").first().attr("href"));
                if (checkJob(job)) {
                    jobs.add(job);
                }
            }
        }
    }

    private boolean checkJob(Job job) {
        // Perform keyword and lookup tests
        if (keywordCheck(job) || alreadyExists(job)) {
            return false;
        }
        return true;
    }

    private boolean keywordCheck(Job job) {
        // Do comparisons
        for (Filter filter : filters) {
            if (filter.getType().equals("title")) {
                    if (job.getTitle().toLowerCase().contains(filter.getKeyword())) {
                        return true;
                    }
                    continue;
            }
            if (filter.getType().equals("location")) {
                if (job.getLocation().toLowerCase().contains(filter.getKeyword())) {
                    return true;
                }
                continue;
            }
            if (filter.getType().equals("company")) {
                if (job.getCompany().toLowerCase().contains(filter.getKeyword())) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }

    private boolean alreadyExists(Job newJob) {
        for (Job oldJob : history) {
            if (newJob.getLink().equals(oldJob.getLink())) {
                return true;
            }
        }
        return false;
    }

    private HashMap<Element, Element> getJobInfo(Document doc) {
        HashMap<Element, Element> jobInfo = new HashMap<Element, Element>();
        Elements jobContainers = doc.getElementsByClass("jobTitleContainer");
        Elements jobLinks = jobContainers.tagName("a");
        Elements companyContainers = doc.getElementsByClass("companyContainer");
        Elements companyLinks = companyContainers.tagName("a");

        for (int i = 0; i < jobLinks.size(); i++) {
            Element jobLink = jobLinks.get(i);
            Element companyLink = companyLinks.get(i);
            jobInfo.put(jobLink, companyLink);
        }
        return jobInfo;
    }

    private void commit(String website) {
        Helper.writeFile(DIR + website + "-history.txt", jobs, website);
        if (METHOD == 1) {
            Helper.writeFile(DIR + "listings.txt", jobs, website);
        } else if (METHOD == 2) {
            Email.sendEmail(EMAIL, PASSWORD, prepEmail(website));
        }
    }

    private String prepEmail(String website) {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<body line-height=\"90%\">");
        sb.append("<h2>" + website.substring(0, 1).toUpperCase() + website.substring(1) + " results</h2>");
        for (Job job : jobs) {
            sb.append("<h4>" + job.getTitle() + "</h4>");
            sb.append("<p>" + job.getCompany() + "</p>");
            sb.append("<p>" + job.getLocation() + "</p>");
            sb.append("<a href=\"" + job.getLink() + "\">Link</a>");
            sb.append("<br /><br />");
        }
        sb.append("</body>");
        sb.append("</html>");

        return sb.toString();
    }

    /*
    ADD
     */
    private  static void addFilter(String keyword, String type) {
        Filter filter = new Filter(keyword.toLowerCase(), type);
        Helper.writeFile(DIR + "filters.txt", filter.toString());
    }

    /*
    HELP
     */
    private static void displayHelp() {
        System.out.println("Usage: JobFinder [OPTION] [PARAMETERS]");
        System.out.println("Find jobs using popular websites like Monster and Indeed");
        System.out.println("JobFinder adds additional filtering options that allow for results that better suit your liking");
        System.out.println("Example: JobFinder monster");
        System.out.println("\nOPTIONS");
        System.out.println("monster" + "\t\t\t\t\t\t\t" + "Search Monster job listings");
        System.out.println("indeed" + "\t\t\t\t\t\t\t" + "Search Indeed job listings");
        System.out.println("both" + "\t\t\t\t\t\t\t" + "Search both Monster and Indeed job listings");
        System.out.println("addFilter" + "\t\t\t\t\t\t" + "Add filter. Requires additional parameter, see PARAMETERS.");
        System.out.println("\nPARAMETERS");
        System.out.println("--title=\"title\"" + "\t\t\t\t\t" + "Specify job title to be filtered");
        System.out.println("--company=\"location\"" + "\t\t\t" + "Specify company to be filtered");
        System.out.println("--location=\"location\"" + "\t\t\t" + "Specify location to be filtered");
        System.out.println("\nNote: Configuration file (config.ini) must be completed prior to use");
    }
}
