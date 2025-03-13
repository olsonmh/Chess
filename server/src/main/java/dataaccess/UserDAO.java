package dataaccess;
import model.UserData;

public interface UserDAO {

    void createUser(UserData user);

    UserData getUser(String username);

    void clearUserData();

    void testSetup();

    void testRemove();
}


//void insertUser(UserData u) throws DataAccessException;