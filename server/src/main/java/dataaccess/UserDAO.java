package dataaccess;
import model.UserData;

public interface UserDAO {


    public void createUser(UserData user);
    public UserData getUser(String username);
    public void clearUserData();

}


//void insertUser(UserData u) throws DataAccessException;