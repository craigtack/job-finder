package main;

public class Path {
    private String dir;

    public Path() {
        dir = Configuration.getConfig().get("path").get("dir");
    }

    public String getDir() {
        return dir;
    }
}
