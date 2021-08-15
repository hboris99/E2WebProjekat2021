public interface IRepository<I> {
    I getID();
    void setID(I id);
    boolean isDeleted();
    void setDeleted(boolean isDeleted);
}
