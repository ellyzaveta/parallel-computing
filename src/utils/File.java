package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class File {

    public static char[] getCharArr(String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath))).toCharArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getFileSizeMB(String filePath) {
        try {
            long bytes = Files.size(Paths.get(filePath));
            return (int) (bytes / 1024 / 1024);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
