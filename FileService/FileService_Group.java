package FileService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class FileService_Group {
    private static FileService_Group instance=null;
    private static String fileName;
    private static File file;
    private FileService_Group(String fileName1){
        fileName="src/" + fileName1;
    }
    private FileService_Group(File file1){
        file=file1;
    }
    public static FileService_Group getInstance(String fileName){
        if(instance == null)
            instance = new FileService_Group(fileName);
        return instance;
    }
    public static FileService_Group getInstance(File file){
        if(instance == null)
            instance = new FileService_Group(file);
        return instance;
    }

    public List<String> getGroups() throws IOException {
        BufferedReader in = new BufferedReader(new FileReader(fileName));
        List<String> all_groups = new ArrayList<String>();
        String line;
        while ((line = in.readLine()) != null)
            all_groups.add(line);
        return all_groups;
    }

    public void setGroups(List<String> all_groups) throws IOException {
        FileWriter out = new FileWriter(fileName,false);
        for (String e : all_groups)
        {
            out.write(e.split(",")[0] + ",");
            for(int i=1; i < e.split(",").length ; i++){
                out.write(e.split(",")[i] + ",");
            }
            out.write("\n");
        }
        out.flush();
        out.close();
    }

}
