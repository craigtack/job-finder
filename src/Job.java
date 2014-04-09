public class Job {
    private String title;
    private String company;
    private String location;
    private String link;

    public Job(String line) {
        String[] array = line.split(";");

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
}
