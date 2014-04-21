package main;

public class Method {
    private String type;

    public Method() {
        type = Configuration.getConfig().get("method").get("option");
    }

    public String getType() {
        return type;
    }
}
