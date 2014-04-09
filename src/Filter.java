public class Filter {
    private String keyword;
    private String type;

    public Filter(String line) {
        String[] array = line.split(";");

        this.keyword = array[0];
        this.type = array[1];
    }

    public Filter(String keyword, String type) {
        this.keyword = keyword;
        this.type = type;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getType() { return type; }

    public String toString() {
        return getKeyword() + ";" + getType() + "\n";
    }
}
