package service;
import service.requestResult.*;

public class ClearService extends Service{
    public void clearAll(){
        userData.clearUserData();
        authData.clearAuthTokens();
        gameData.clearGames();
    }
}
