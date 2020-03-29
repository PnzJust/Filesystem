package FileSystem;

public class User_File extends File{
    public User_File(String name) {
        this.name = name;
    }

    @Override
    int getRights() {
        return this.rights;
    }

    @Override
    void setRights(int rights) {
        this.rights=rights;
    }

    @Override
    public void setRights() {
        this.rights=770;
    }

    @Override
    public String getName() {
        return this.name + " <- user file";
    }
}
