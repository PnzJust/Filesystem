package FileSystem;

import FileService.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class Clasa_Serviciu {
    private LinkedHashSet<User> all_users = new LinkedHashSet<>(); //all users
    private LinkedHashSet<Group> all_groups = new LinkedHashSet<>(); //all groups
    private TreeSet<String> all_users_name = new TreeSet<>();
    private TreeSet<String> all_groups_name = new TreeSet<>();
    private Root root = Root.getInstance();
    private Main_Directory main_directory = Main_Directory.getInstance();
    private LinkedHashSet<Directory> all_directories = new LinkedHashSet<>(); //all dirs
    final String url = "jdbc:mysql://localhost:3306/test?serverTimezone=UTC";
    final String username = "root";
    final String password = "";

    FileService_User  Service_User =  FileService_User.getInstance("users.csv");
    FileService_Group Service_Group =  FileService_Group.getInstance("groups.csv");
    FileService_File Service_Files =  FileService_File.getInstance("files.csv");
    FileService_Directory Service_Directory =  FileService_Directory.getInstance("directories.csv");
    FileService_Logs Service_Logs = FileService_Logs.getInstance("logs.csv");

    public Clasa_Serviciu(){
        try (Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
             Statement statement = connection.createStatement()){
            all_directories.add(main_directory);
            File I = new Kernel_File("I");
            main_directory.addFile(I);

            // Citire Useri din BD
            String query = "SELECT * FROM test.users";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next())
            {
                User new_user = new User(resultSet.getString("name"));
                all_users.add(new_user);
                all_users_name.add(new_user.getName());
            }

            // Citire grupuri din BD
            query = "SELECT * FROM test.groups";
            resultSet = statement.executeQuery(query);
            while (resultSet.next())
            {
                String group_name = resultSet.getString("name");
                Group new_group = new Group(group_name);
                String group_query = "SELECT * FROM test.group_" + group_name;
                Statement statement1 = connection.createStatement();
                ResultSet querySet = statement1.executeQuery(group_query);
                while(querySet.next())
                {
                    group_name = querySet.getString("name");
                    User new_user = null;
                    for (User user: all_users)
                        if (user.getName().equals(group_name))
                            new_user = user;
                    if(new_user!=null)
                        new_group.addUser(new_user);
                    else
                    {
                        new_user = new User(group_name);
                        new_group.addUser(new_user);
                        all_users.add(new_user);
                        all_users_name.add(new_user.getName());
                    }
                }

                all_groups.add(new_group);
                all_groups_name.add(new_group.getNume());
            }

            // Citire directoare din BD
            query = "SELECT * FROM test.dirs";
            resultSet = statement.executeQuery(query);
            while (resultSet.next())
            {
                this.createDir(resultSet.getString("name"), resultSet.getString("location"));
            }
            // Citire fisiere din BD
            query = "SELECT * FROM test.files";
            resultSet = statement.executeQuery(query);
            while (resultSet.next())
            {
                this.createFile(resultSet.getString("name"), resultSet.getString("location"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.exit(1);
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
        try (Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
             Statement statement = connection.createStatement())
        {
            String query = "INSERT INTO `test`.`users` (`name`) VALUES ('" + nume + "');";
            statement.executeUpdate(query);
            Service_Logs.addEvent("S-a adaugat userul " + nume);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.exit(1);
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
        try (Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
             Statement statement = connection.createStatement())
        {
            String query = "INSERT INTO `test`.`groups` (`name`) VALUES ('" + nume + "');";
            statement.executeUpdate(query);
            query = "CREATE TABLE `test`.`group_" + nume+ "` (" +
                    "  `name` INT NOT NULL," +
                    "  PRIMARY KEY (`name`));";
            statement.executeUpdate(query);
            Service_Logs.addEvent("S-a adaugat fisierul " + nume);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.exit(1);
        }
        catch (IOException e)
        {
            System.out.println("Nu se poate scrie in fisier:\n" + e);
        }
    }
    public void moveUser(String user, String group){
        Group actual=null,urmator=null;
        String nume_actual="", nume_urmator="";
        User X = null;
        for(Group grup:all_groups) {
            if (grup.getNume().equals(group)) {
                urmator = grup;
                nume_urmator = urmator.getNume();
            }
            for(User user1:grup.getUsers())
            {
                if (user1.getName().equals(user)) {
                    actual = grup;
                    nume_actual = grup.getNume();
                    X = user1;
                    break;
                }
            }
        }
        try (Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
             Statement statement = connection.createStatement())
        {
        if (actual != null)
        {
            actual.removeUser(X);
            statement.executeUpdate("DELETE FROM `test`.`group_" +nume_actual + "` WHERE (`name` = '" + user + "');");
        }
        if (X == null)
            X = new User(user);
        if (urmator != null)
        {
            urmator.addUser(X);
            statement.executeUpdate("INSERT INTO `test`.`group_" + nume_urmator + "` (`name`) VALUES ('"+user+"');");
        }

        Service_Logs.addEvent("S-a mutal userul " + user + " in grupul " + group);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.exit(1);
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

        try (Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
              Statement statement = connection.createStatement())
        {
            String query = "INSERT INTO `test`.`files` (`name`,`location`) VALUES ('" + nume + "','" +locatie + "');";
            statement.executeUpdate(query);
            Service_Logs.addEvent("S-a creat fisierul " + nume + " in " +locatie);
        }
        catch (SQLException e)
        {
            e.printStackTrace();
            System.exit(1);
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
                    try (Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
                         Statement statement = connection.createStatement())
                    {
                        String query = "DELETE FROM `test`.`users` WHERE (`name` = '" +name + "');";
                        statement.executeUpdate(query);
                        Service_Logs.addEvent("S-a sters userul " + name);
                    }
                    catch (SQLException exc)
                    {
                        exc.printStackTrace();
                        System.exit(1);
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
                try (Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
                     Statement statement = connection.createStatement())
                {
                    String query = "DELETE FROM `test`.`groups` WHERE (`name` = '" +name + "');";
                    statement.executeUpdate(query);
                    query = "DROP TABLE test.group_" + name + ";";
                    statement.executeUpdate(query);
                    Service_Logs.addEvent("S-a sters grupul " + name);
                }
                catch (SQLException exc)
                {
                    exc.printStackTrace();
                    System.exit(1);
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
                try (Connection connection = DriverManager.getConnection(this.url, this.username, this.password);
                     Statement statement = connection.createStatement())
                {
                    String query = "DELETE FROM `test`.`dirs` WHERE (`name` = '" +name + "');";
                    statement.executeUpdate(query);
                    Service_Logs.addEvent("S-a sters directorul " + name);
                }
                catch (SQLException exc)
                {
                    exc.printStackTrace();
                    System.exit(1);
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
