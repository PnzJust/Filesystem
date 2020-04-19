package FileSystem;

// clasa singleton
public class Root extends User{
    private static Root instance = null;
    private Root(){};
    public static Root getInstance(){
        if(instance == null)
        {
            instance = new Root();
            instance.setName(null);
        }
        return instance;
    }

    @Override
    public void deleteFile(Kernel_File file) {
            String nume = file.getName();
            file = null;
            System.gc();
            System.out.println(nume + "Sters");
    }
}
