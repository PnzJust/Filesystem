package FileService;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

public class FileService_Logs {
    private static FileService_Logs instance=null;
    private static String fileName;
    private static File file;
    private FileService_Logs(String fileName1){
        fileName="src/" + fileName1;
    }
    private FileService_Logs(File file1){
        file=file1;
    }
    public static FileService_Logs getInstance(String fileName){
        if(instance == null)
            instance = new FileService_Logs(fileName);
        return instance;
    }
    public static FileService_Logs getInstance(File file){
        if(instance == null)
            instance = new FileService_Logs(file);
        return instance;
    }

    public void addEvent(String msg) throws IOException {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        FileWriter out = new FileWriter(fileName,true);
        out.write("TImp: " + formatter.format(date) + ",");
        out.write(msg + "\n");
        out.flush();
        out.close();
    }

}
