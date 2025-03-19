package dataaccess;
import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class MemoryUserDAO implements UserDAO {
    private final Map<String, UserData> memoryUserData;

    public MemoryUserDAO(){
        this.memoryUserData = new HashMap<>();
    }

    @Override
    public void createUser(UserData user) {
        this.memoryUserData.put(user.username(),user);
    }

    @Override
    public UserData getUser(String username) {
        return this.memoryUserData.get(username);
    }

    @Override
    public void clearUserData() {
        memoryUserData.clear();
    }

    public void testSetup(){}

    public void testRemove(){}
}
