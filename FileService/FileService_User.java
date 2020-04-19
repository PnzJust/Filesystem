package FileService;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class FileService_User {
    private static FileService_User instance=null;
    private static String fileName;
    private static File file;
    private FileService_User(String fileName1){
        fileName="src/" + fileName1;
    }
    private FileService_User(File file1){
        file=file1;
    }
    public static FileService_User getInstance(String fileName){
        if(instance == null)
            instance = new FileService_User(fileName);
        return instance;
    }
    public static FileService_User getInstance(File file){
        if(instance == null)
            instance = new FileService_User(file);
        return instance;
    }

    public List<String> getUsers() throws IOException {
        BufferedReader in = new BufferedReader(new  FileReader(fileName));
        List<String> all_users = new ArrayList<String>();
        String line;
        while ((line = in.readLine()) != null)
            for (String e: line.split(","))
                all_users.add(e);
        return all_users;
    }
    public void setUsers(TreeSet<String> all_users) throws IOException {
        FileWriter out = new FileWriter(fileName,false);
        for (String e : all_users)
            out.write(e+",");
        out.flush();
        out.close();
    }

}
