package FileSystem;

import java.util.ArrayList;
import java.util.List;

public class Directory {
    protected List<Directory> subdirectories = new ArrayList<>();
    protected List<File> files = new ArrayList<>();
    protected String name;
    public Directory(){}
    public Directory(String name){
        this.name=name;
    }
    public String getName(){
        return this.name;
    }
    public List<Directory> getSubdirectories() {
        return subdirectories;
    }
    public List<File> getFiles() {
        return files;
    }
    public void setName(String name){
        this.name = name;
    }
    void addSubdirectory(Directory X){
        this.subdirectories.add(X);
    }
    void addFile(File X){
        this.files.add(X);
    }
}
