package main;

import org.jsoup.nodes.Document;

import java.lang.reflect.Array;
import java.util.ArrayList;

public abstract class Website {
    protected String name;
    protected String url;
    protected int numOfPages;
    protected ArrayList<String> pages;
    protected ArrayList<Job> history;

    public String getUrl() {
        return url;
    }

    public String getName() {
        return name;
    }

    public int getNumOfPages() {
        return numOfPages;
    }

    public ArrayList<String> getPages() {
        return pages;
    }

    public ArrayList<Job> getHistory() {
        return history;
    }
}
