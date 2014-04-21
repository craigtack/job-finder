package main;

import utility.FileIO;

import java.util.ArrayList;

public class Job {
    private String title;
    private String company;
    private String location;
    private String link;

    public Job(String line) {
        String[] array = line.split(";");

        if (array.length != 4) {
            // TODO: take action here
        }

        this.title = array[0];
        this.company = array[1];
        this.location = array[2];
        this.link = array[3];
    }
    public Job(String title, String company, String location, String link) {
        this.title = title;
        this.company = company;
        this.location = location;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public String getCompany() {
        return company;
    }

    public String getLocation() { return location; }

    public String getLink() {
        return link;
    }

    public String toString() {
        return getTitle() + ";" + getCompany() + ";" + getLocation() + ";" + getLink() + "\n";
    }

    public static void writeHistory(final String fileName, final ArrayList<Job> jobs) {
        ArrayList<String> lines = new ArrayList<String>();
        for (Job job : jobs) {
            lines.add(job.toString());
        }
        FileIO.writeFile(fileName, lines);
    }

    public static void writeListings(final String fileName, final ArrayList<Job> jobs, final String website) {
        ArrayList<String> lines = new ArrayList<String>();
        lines.add(website.substring(0, 1).toUpperCase() + website.substring(1) + " results\n\n");
        for (Job job : jobs) {
            lines.add(job.getTitle() + "\n" + job.getCompany() + "\n" + job.getLocation() + "\n" +
                    job.getLink() + "\n\n");
        }
        FileIO.writeFile(fileName, lines);
    }
}
