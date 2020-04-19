package FileSystem;

abstract class File {
    protected String name;
    protected int rights;
//    rights  -> 100 -> read
//            -> 010 -> write
//            -> 001 -> delete
    User owner;

    abstract public String getName();
    abstract int getRights();
    User getOwner(){
        return owner;
    }
    abstract void setRights(int rights);
    abstract void setRights();
    void setOwner(User owner) {
        this.owner = owner;
    }
}
