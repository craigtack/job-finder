package utility;

import main.Configuration;
import main.Path;

import java.io.*;
import java.util.ArrayList;

public class FileIO {
    private FileIO(){}

    public static <T> ArrayList<T> readFile(final Class<T> clazz, final String fileName) {
        ArrayList<T> list = new ArrayList<T>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(new File(new Path().getDir() + fileName)));

            String line = null;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }
                list.add(clazz.getConstructor(String.class).newInstance(line));
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void writeLine(final String fileName, final String line) {
        try {
            FileWriter writer = new FileWriter(new File(fileName), true);
            writer.write(line);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(final String fileName, final ArrayList<String> lines) {
        try {
            FileWriter writer = new FileWriter(new File(fileName), true);
            for (String line : lines) {
                writer.write(line);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
