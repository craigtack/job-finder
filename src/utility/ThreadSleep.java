package utility;

public class ThreadSleep {
    private ThreadSleep(){}

    public static void sleep() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
