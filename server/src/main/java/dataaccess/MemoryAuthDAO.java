package dataaccess;
import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class MemoryAuthDAO implements AuthDAO{
    private final Map<String, AuthData> memoryAuthData;

    public MemoryAuthDAO(){
        this.memoryAuthData = new HashMap<>();
    }

    @Override
    public void createAuth(AuthData authData) {
        this.memoryAuthData.put(authData.authToken(),authData);
    }

    @Override
    public AuthData getAuth(String authToken) {
        return this.memoryAuthData.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        this.memoryAuthData.remove(authToken);
    }

    @Override
    public void clearAuthTokens() {
        this.memoryAuthData.clear();
    }
}
