package FileSystem;

import java.util.*;

public class Clasa_Serviciu {
    private LinkedHashSet<User> all_users = new LinkedHashSet<>(); //all users
    private LinkedHashSet<Group> all_groups = new LinkedHashSet<>(); //all groups
    private TreeSet<String> all_users_name = new TreeSet<>();
    private TreeSet<String> all_groups_name = new TreeSet<>();
    private Root root;
    private Main_Directory main_directory;
    private LinkedHashSet<Directory> all_directories = new LinkedHashSet<>(); //all dirs
    public Clasa_Serviciu(){
        root = new Root();
        User A1 = new User("A1");
        User A2 = new User("A2");
        all_users.add(A1);all_users.add(A2);
        all_users_name.add(A1.getName());all_users_name.add(A2.getName());
        Group A = new Group("A");
        A.addUser(A1);A.addUser(A2);
        all_groups.add(A);
        all_groups_name.add(A.getNume());

        main_directory = new Main_Directory();
        Directory D = new Directory("D"), E = new Directory("E"), F = new Directory("F");
        Main_Directory.addSubdirectory_to_main(D);
        Main_Directory.addSubdirectory_to_main(E);
        D.addSubdirectory(F);
        all_directories.add(E);all_directories.add(D);all_directories.add(F);
        File G = new User_File("G"), H = new User_File("H"), I = new Kernel_File("I");
        G.setRights(1770);
        H.setRights();
        I.setRights(1000);
        Main_Directory.addFile_to_main(I);
        D.addFile(G);
        D.addFile(H);
    }
    public void addUser(String nume) {
        if (all_users_name.contains(nume)) {
            System.out.println("Exista deja");
            return;
        }
        User nou = new User(nume);
        all_users.add(nou);
        all_users_name.add(nume);
    }
    public void addGroup(String nume) {
        if (all_groups_name.contains(nume)) {
            System.out.println("Exista deja");
            return;
        }
        Group nou = new Group(nume);
        all_groups.add(nou);
        all_groups_name.add(nume);
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
                nou.addSubdirectory(n);
                all_directories.add(n);
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
    }
    public void showUsers(){
        for( User e: all_users)
            System.out.println(e.getName());
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
        for(File e:main_directory.getFiles_to_main())
            System.out.println("/" + e.getName());
        for(Directory e:main_directory.getSubdirectories_to_main())
            f("/" + e.getName() + "/",e);
    }
    public void deleteUser(String name){
        for (User e:all_users)
        {
            if (e.getName().equals(name))
                {
                    all_users_name.remove(e.getName());
                    all_users.remove(e);
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
                return;
            }
        }
        System.out.println("nu exista userul");
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
