package FileSystem;

public class Kernel_File extends File{
    public Kernel_File(String name) {
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
    void setRights(){
        this.rights=0;
    }
    @Override
    public String getName() {
        return this.name + " <- kernel file";
    }
}
