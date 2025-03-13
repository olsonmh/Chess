package service;

public class ClearService extends Service{
    public void clearAll(){
        authData.clearAuthTokens();
        gameData.clearGames();
        userData.clearUserData();
    }
}
