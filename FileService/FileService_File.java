package FileService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class FileService_File {
    private static FileService_File instance=null;
    private static String fileName;
    private static File file;
    private FileService_File(String fileName1){
        fileName="src/" + fileName1;
    }
    private FileService_File(File file1){
        file=file1;
    }
    public static FileService_File getInstance(String fileName){
        if(instance == null)
            instance = new FileService_File(fileName);
        return instance;
    }
    public static FileService_File getInstance(File file){
        if(instance == null)
            instance = new FileService_File(file);
        return instance;
    }

    public List<String> getFiles() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        List<String> all_files = new ArrayList<String>();
        String line;
        while ((line = in.readLine()) != null)
            all_files.add(line);
        return all_files;
    }
}
