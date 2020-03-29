package FileSystem;

import java.util.ArrayList;
import java.util.List;

public class Main_Directory extends Directory {
    private static Main_Directory instance = null;
    static List<Directory> subdirectories_to_main = new ArrayList<>();
    static List<File> files_to_main = new ArrayList<>();
    public static Main_Directory getInstance() {
        if (instance == null) {
            instance = new Main_Directory();
        }
        return instance;
    }

    static void addSubdirectory_to_main(Directory X) {
        instance.subdirectories_to_main.add(X);
    }

    static void addFile_to_main(File X) {
        instance.files_to_main.add(X);
    }

    public static List<File> getFiles_to_main() {
        return instance.files_to_main;
    }

    public static List<Directory> getSubdirectories_to_main() {
        return instance.subdirectories_to_main;
    }

}