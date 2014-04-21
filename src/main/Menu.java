package main;

public class Menu {

    public Menu(String[] args) {
        if (handleHelp(args)) {
            return;
        } else if (handleSelection(args)) {
            return;
        } else if (handleFilters(args)) {
            return;
        }
    }

    private boolean handleHelp(String[] args) {
        if (args[0].equals("help") || args.length == 0) {
            JobFinder.displayHelp();
            return true;
        }
        return false;
    }


    private boolean handleSelection(String[] args) {
        if (args[0].equals("both")) {
            new JobFinder(new Monster());
            new JobFinder(new Indeed());
            return true;
        } else if (args[0].equals("monster")) {
            new JobFinder(new Monster());
            return true;
        } else if (args[0].equals("indeed")) {
            new JobFinder(new Indeed());
            return true;
        }
        return false;
    }

    private boolean handleFilters(String[] args) {
        if (args[0].equals("addFilter")) {
            if (args.length == 1) {
                System.out.println("Error: addFilter requires additional parameter\n" +
                        "Try 'JobFinder help' for more information");
                return true;
            } else {
                if (args[1].contains("--title=")) {
                    new Filter(args[1].substring(8, args[1].length()), "title").add();
                } else if (args[1].contains("--company=")) {
                    new Filter(args[1].substring(10, args[1].length()), "company").add();
                } else if (args[1].contains("--location=")) {
                    new Filter(args[1].substring(11, args[1].length()), "location").add();
                }
                return true;
            }
        }
        return false;
    }
}
