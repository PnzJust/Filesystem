package FileService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class FileService_Directory {
    private static FileService_Directory instance=null;
    private static String fileName;
    private static File file;
    private FileService_Directory(String fileName1){
        fileName="src/" + fileName1;
    }
    private FileService_Directory(File file1){
        file=file1;
    }
    public static FileService_Directory getInstance(String fileName){
        if(instance == null)
            instance = new FileService_Directory(fileName);
        return instance;
    }
    public static FileService_Directory getInstance(File file){
        if(instance == null)
            instance = new FileService_Directory(file);
        return instance;
    }

    public List<String> getDirs() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        List<String> all_dirs = new ArrayList<String>();
        String line;
        while ((line = in.readLine()) != null)
            all_dirs.add(line);
        return all_dirs;
    }
}
