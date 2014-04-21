package main;

import org.jsoup.nodes.Document;
import utility.FileIO;
import utility.GetDocument;

import java.util.ArrayList;

public class JobFinder {
    private Website website;
    private ArrayList<Job> foundJobs;
    private ArrayList<Filter> filters;
    private ArrayList<Document> scrapedPages;

    public JobFinder(Website website) {
        this.website = website;
        filters = FileIO.readFile(Filter.class, "filters.txt");
        scrapedPages = new ArrayList<Document>();

        scrape();
    }

    public static void main(String[] args) {
        new Configuration("src\\main\\config.ini");
        new Menu(args);
    }

    private void continueEvents() {
        parseHTML();
        sanityCheck();
        commit();
    }

    private void scrape() {
        for (int i = 0; i < website.getPages().size(); i++) {
            scrapedPages.add(GetDocument.get(website.getPages().get(i)));
        }

        continueEvents();
    }

    private void parseHTML() {
        if (website.getName().equals("monster")) {
            foundJobs = Monster.parseHTML(scrapedPages);
        } else if (website.getName().equals("indeed")) {
            foundJobs = Indeed.parseHTML(scrapedPages);
        }
    }

    private void sanityCheck() {
        for (int i = 0; i < foundJobs.size(); i++) {
            if (inHistory(foundJobs.get(i)) || matchesFilter(foundJobs.get(i))) {
                foundJobs.remove(i);
                i = i == 0 ? i - 1 : i - 2;
            }
        }
    }

    private boolean inHistory(Job newJob) {
        for (Job oldJob : website.getHistory()) {
            if (newJob.getLink().equals(oldJob.getLink())) {
                return true;
            }
        }
        return false;
    }

    private boolean matchesFilter(Job newJob) {
        for (Filter filter : filters) {
            if (filter.getType().equals("title") &&
                    newJob.getTitle().toLowerCase().contains(filter.getKeyword())) {
                    return true;
            } else if (filter.getType().equals("location") &&
                    newJob.getLocation().toLowerCase().contains(filter.getKeyword())) {
                    return true;
            } else if (filter.getType().equals("company") &&
                    newJob.getCompany().toLowerCase().contains(filter.getKeyword())) {
                    return true;
            }
        }
        return false;
    }

    private void commit() {
        if (foundJobs.isEmpty()) {
            return;
        }
        Path path = new Path();
        Method method = new Method();

        Job.writeHistory(path.getDir() + website.getName() + "-history.txt", foundJobs);
        if (method.getType().equals("file")) {
            Job.writeListings(path.getDir() + "listings.txt", foundJobs, website.getName());
        } else if (method.getType().equals("email")) {
            new EmailAccount().sendEmail(buildBody());
        }
    }

    private String buildBody() {
        StringBuilder sb = new StringBuilder();
        sb.append("<html>");
        sb.append("<body line-height=\"90%\">");
        sb.append("<h2>" + website.getName().substring(0, 1).toUpperCase() +
                website.getName().substring(1) + " results</h2>");
        for (Job job : foundJobs) {
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

    public static void displayHelp() {
        System.out.println("Usage: main.JobFinder [OPTION] [PARAMETERS]");
        System.out.println("Find jobs using popular websites like Monster and Indeed");
        System.out.println("main.JobFinder adds additional filtering options that allow for results that better suit your liking");
        System.out.println("Example: main.JobFinder monster");
        System.out.println("\nOPTIONS");
        System.out.println("monster" + "\t\t\t\t\t\t\t" + "Search Monster job listings");
        System.out.println("indeed" + "\t\t\t\t\t\t\t" + "Search Indeed job listings");
        System.out.println("both" + "\t\t\t\t\t\t\t" + "Search both Monster and Indeed job listings");
        System.out.println("addFilter" + "\t\t\t\t\t\t" + "Add filter. Requires additional parameter, see PARAMETERS.");
        System.out.println("\nPARAMETERS");
        System.out.println("--title=title" + "\t\t\t\t\t" + "Specify job title to be filtered");
        System.out.println("--company=company" + "\t\t\t" + "Specify company to be filtered");
        System.out.println("--location=location" + "\t\t\t" + "Specify location to be filtered");
        System.out.println("\nNote: Configuration file (config.ini) must be completed prior to use");
    }
}
