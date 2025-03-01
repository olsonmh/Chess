package dataaccess;
import model.AuthData;

public interface AuthDAO {
    void createAuth(AuthData authdata);

    AuthData getAuth(String authToken);

    void deleteAuth(String authToken);

    void clearAuthTokens();
}
