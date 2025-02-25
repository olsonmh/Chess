package dataaccess;
import model.AuthData;

public interface AuthDAO {
    public AuthData createAuth(AuthData authdata);

    public AuthData getAuth(String authToken);

    public void deleteAuth(String authToken);

    public void clearAuthTokens();
}
