package FileSystem;

public class User {
    protected String name;

    public User(){}
    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public User_File createFile(String nume)
    {
        User_File obj = new User_File(nume);
        obj.setRights();
        obj.setOwner(this);
        return obj;
    }
    public void deleteFile(User_File file)
    {
        int user_rights = file.getRights() / 10 % 10;
        if (file.getOwner().getName().equals(this.name))
        {
            if( (user_rights&1) == 1) {
                String nume = file.getName();
                file = null;
                System.gc();
                System.out.println(nume + "Sters");
                return;
            }
        }
        System.out.println(this.name + " nu are drepturile necesare asupra lui " +file.getName() );
    }
    public void deleteFile(Kernel_File file)
    {
        System.out.println(this.name + " nu are drepturile necesare asupra lui " +file.getName() );
    }
}
