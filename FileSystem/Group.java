package FileSystem;

import java.util.ArrayList;
import java.util.List;

public class Group {
    private String nume;
    private List<User> users = new ArrayList<>();

    public Group(String nume) {
        this.nume = nume;
    }
    public String getNume() {
        return nume;
    }
    public List<User> getUsers() {
        return users;
    }
    public void setNume(String nume) {
        this.nume = nume;
    }
    public void addUser(User user){
        this.users.add(user);
    }
    public void removeUser(User user){
        this.users.remove(user);
    }

}
