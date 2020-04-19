package FileSystem;

import java.util.ArrayList;
import java.util.List;

public class Main_Directory extends Directory {
    private static Main_Directory instance = null;
    public List<Directory> subdirectories_to_main = new ArrayList<>();
    public List<File> files_to_main = new ArrayList<>();
    private  Main_Directory(){};;

    public static Main_Directory getInstance() {
        if (instance == null) {
            instance = new Main_Directory();
            instance.name="/";
        }
        return instance;
    }

    public void addSubdirectory(Directory X) {
        instance.subdirectories_to_main.add(X);
    }

    public void addFile(File X) {
        instance.files_to_main.add(X);
    }

    public  List<File> getFiles() {
        return instance.files_to_main;
    }

    public List<Directory> getSubdirectories() {
        return instance.subdirectories_to_main;
    }

}