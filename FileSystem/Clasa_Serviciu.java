package FileSystem;

import FileService.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class Clasa_Serviciu {
    private LinkedHashSet<User> all_users = new LinkedHashSet<>(); //all users
    private LinkedHashSet<Group> all_groups = new LinkedHashSet<>(); //all groups
    private TreeSet<String> all_users_name = new TreeSet<>();
    private TreeSet<String> all_groups_name = new TreeSet<>();
    private Root root = Root.getInstance();
    private Main_Directory main_directory = Main_Directory.getInstance();
    private LinkedHashSet<Directory> all_directories = new LinkedHashSet<>(); //all dirs

    FileService_User  Service_User =  FileService_User.getInstance("users.csv");
    FileService_Group Service_Group =  FileService_Group.getInstance("groups.csv");
    FileService_File Service_Files =  FileService_File.getInstance("files.csv");
    FileService_Directory Service_Directory =  FileService_Directory.getInstance("directories.csv");
    FileService_Logs Service_Logs = FileService_Logs.getInstance("logs.csv");
    public Clasa_Serviciu(){
        try {
            all_directories.add(main_directory);
            File I = new Kernel_File("I");
            main_directory.addFile(I);

            // Citire Useri din fisier
            for (String e: Service_User.getUsers()) {
                User new_user = new User(e);
                all_users.add(new_user);
                all_users_name.add(new_user.getName());
            }
            // Citire grupuri din fisier
            for (String e: Service_Group.getGroups()) {
                Group new_group = new Group(e.split(",")[0]);
                for( int i=1; i<e.split(",").length ; i++)
                {
                    User new_user = null;
                    for (User user: all_users)
                        if (user.getName().equals(e.split(",")[i]))
                            new_user = user;
                    if(new_user!=null)
                        new_group.addUser(new_user);
                    else
                    {
                        new_user = new User(e.split(",")[i]);
                        new_group.addUser(new_user);
                        all_users.add(new_user);
                        all_users_name.add(new_user.getName());
                    }
                }
                all_groups.add(new_group);
                all_groups_name.add(new_group.getNume());
            }
            // Citire directoare din fisier
            for (String e: Service_Directory.getDirs())
                this.createDir(e.split(",")[0],e.split(",")[1]);
            // Citire fisiere din fisier
            for (String e: Service_Files.getFiles())
                this.createFile(e.split(",")[0],e.split(",")[1]);
        }
        catch (FileNotFoundException e){
            System.out.println("Nu exista fisierul:\n" + e);
        }
        catch (IOException e){
            System.out.println("Nu se poate citi din fisier:\n" + e);
        }
    }
    public void addUser(String nume) {
        if (all_users_name.contains(nume)) {
            System.out.println("Exista deja");
            return;
        }
        User nou = new User(nume);
        all_users.add(nou);
        all_users_name.add(nume);
        try
        {
            Service_Logs.addEvent("S-a adaugat userul " + nume);
        }
        catch (IOException e)
        {
            System.out.println("Nu se poate scrie in fisier:\n" + e);
        }
    }
    public void addGroup(String nume) {
        if (all_groups_name.contains(nume)) {
            System.out.println("Exista deja");
            return;
        }
        Group nou = new Group(nume);
        all_groups.add(nou);
        all_groups_name.add(nume);
        try
        {
            Service_Logs.addEvent("S-a adaugat fisierul " + nume);
        }
        catch (IOException e)
        {
            System.out.println("Nu se poate scrie in fisier:\n" + e);
        }
    }
    public void moveUser(String user, String group){
        Group actual=null,urmator=null;
        User X = null;
        for(Group grup:all_groups) {
            if (grup.getNume().equals(group)) {
                urmator = grup;
            }
            for(User user1:grup.getUsers())
            {
                if (user1.getName().equals(user)) {
                    actual = grup;
                    X = user1;
                    break;
                }
            }
        }
        if (actual != null)
            actual.removeUser(X);
        if (X == null)
            X = new User(user);
        if (urmator != null)
            urmator.addUser(X);

        try
        {
            Service_Logs.addEvent("S-a mutal userul " + user + " in grupul " + group);
        }
        catch (IOException e)
        {
            System.out.println("Nu se poate scrie in fisier:\n" + e);
        }
    }
    public void createDir(String nume, String locatie){
        Directory nou = null;
        for(Directory dir:all_directories) {
            if (dir.getName().equals(locatie))
                nou = dir;
        }
        if (nou != null)
            {
                Directory n = new Directory(nume);
                all_directories.add(n);
                if (nou.getName().equals("/"))
                    main_directory.addSubdirectory(n);
                else
                    nou.addSubdirectory(n);
            }
        try
        {
            Service_Logs.addEvent("S-a creat directorul " + nume + " in " + locatie);
        }
        catch (IOException e)
        {
            System.out.println("Nu se poate scrie in fisier:\n" + e);
        }
    }
    public void createFile(String nume, String locatie){
        Directory nou = null;
        for(Directory dir:all_directories) {
            if (dir.getName().equals(locatie))
                nou = dir;
        }
        if (nou != null)
            nou.addFile(new User_File(nume));
        try
        {
            Service_Logs.addEvent("S-a creat fisierul " + nume + " in " +locatie);
        }
        catch (IOException e)
        {
            System.out.println("Nu se poate scrie in fisier:\n" + e);
        }
    }
    public void showUsers(){
        for( User e: all_users)
            System.out.println(e.getName());
        try
        {
            Service_Logs.addEvent("S-a afisat userii");
        }
        catch (IOException e)
        {
            System.out.println("Nu se poate scrie in fisier:\n" + e);
        }
    }
    public void showGroups(){
        for( Group e: all_groups)
            {
                String info = "Grupul: ";
                info = info + e.getNume() + " cu userii: ";
            for (User x:e.getUsers())
                info = info + x.getName() + " ";
                System.out.println(info);
            }
        try
        {
            Service_Logs.addEvent("S-a afisat grupurile ");
        }
        catch (IOException e)
        {
            System.out.println("Nu se poate scrie in fisier:\n" + e);
        }
    }
    public void f(String name, Directory dir) {
        for(File e:dir.getFiles())
            System.out.println(name + e.getName() + " <- fisier");
        for(Directory e:dir.getSubdirectories())
                f(name + e.getName()+ "/" , e);
    }
    public void showFiles(){
        String info = "Directoarele:";
        for (Directory e: all_directories)
            info = info + " " + e.getName();
        System.out.println(info);
        for(File e:main_directory.getFiles())
            System.out.println("/" + e.getName());
        for(Directory e:main_directory.getSubdirectories())
            f("/" + e.getName() + "/",e);

        try
        {
            Service_Logs.addEvent("S-a afisat fisierele ");
        }
        catch (IOException e)
        {
            System.out.println("Nu se poate scrie in fisier:\n" + e);
        }
    }
    public void deleteUser(String name){
        for (User e:all_users)
        {
            if (e.getName().equals(name))
                {
                    all_users_name.remove(e.getName());
                    all_users.remove(e);
                    try
                    {
                        Service_Logs.addEvent("S-a sters userul " + name);
                    }
                    catch (IOException x)
                    {
                        System.out.println("Nu se poate scrie in fisier:\n" + e);
                    }
                    return;
                }
        }
        System.out.println("nu exista userul");
    }
    public void deleteGroup(String name){
        for (Group e:all_groups)
        {
            if (e.getNume().equals(name))
            {
                all_groups_name.remove(e.getNume());
                all_groups.remove(e);
                try
                {
                    Service_Logs.addEvent("S-a sters grupul " + name);
                }
                catch (IOException x)
                {
                    System.out.println("Nu se poate scrie in fisier:\n" + e);
                }
                return;
            }
        }
        System.out.println("nu exista grupul");
    }
    public void deleteDir(String name){
        for (Directory e:all_directories)
        {
            if (e.getName().equals(name))
            {
                if (e.getFiles().size()!=0)
                {
                    System.out.println("Folderul nu este gol\nContine");
                    for(Directory x:e.getSubdirectories())
                        System.out.println(x.getName() + "-subdirector");
                    for(File x:e.getFiles())
                        System.out.println(x.getName() + "-fisier");
                    return;
                }
                all_directories.remove(e);
                try
                {
                    Service_Logs.addEvent("S-a sters directorul " + name);
                }
                catch (IOException x)
                {
                    System.out.println("Nu se poate scrie in fisier:\n" + e);
                }
                return;
            }
        }
        System.out.println("nu exista userul");
    }
    public void finalizare(){
        // Afisare grupuri in fisier:
        try {
            List<String> groups_write = new ArrayList<>();
            for(Group e: all_groups)
            {
                String nume = "";
                nume = nume + e.getNume() + ",";
                for (User x : e.getUsers())
                    nume = nume + x.getName() + ",";
                groups_write.add(nume);
            }
            this.Service_Group.setGroups(groups_write);
            this.Service_User.setUsers(all_users_name);
        }
        catch (FileNotFoundException e){
            System.out.println("Nu exista fisierul:\n" + e);
        }
        catch (IOException e){
            System.out.println("Nu se poate citi din fisier:\n" + e);
        }
    }
    public void creare_stergere(){
        User X = new User("X");
        Kernel_File Y = new Kernel_File("ABC"); // am creat un fisier al kernelului
        User_File new_file = X.createFile("ABCD"); // am creat un fisier al utilizatorului X
        X.deleteFile(new_file);
        X.deleteFile(Y);
        root.deleteFile(Y);
    }
}
