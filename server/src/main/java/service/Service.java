package service;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import service.exceptions.AuthTokenNotFoundException;
import service.exceptions.GameNotFoundException;

import java.util.UUID;
import java.util.Random;

public abstract class Service {
    //protected static AuthDAO authData = new MemoryAuthDAO(); protected static GameDAO gameData = new MemoryGameDAO();
    protected static AuthDAO authData = new MySqlDataAccess();
    protected static GameDAO gameData = (GameDAO)authData;
    protected static UserDAO userData = (UserDAO)gameData;
    //protected static UserDAO userData = new MemoryUserDAO();


    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public String getUsername(String authToken) throws AuthTokenNotFoundException{
        AuthData auth = authData.getAuth(authToken);
        if (auth == null){
            throw new AuthTokenNotFoundException("Authken not found\n");
        }
        return auth.username();
    }

    public GameData getGame(int gameID) throws GameNotFoundException {
        return gameData.getGame(gameID);
    }

    public void updateGame(GameData game){
        gameData.updateGame(game);
    }

    public static int generateGameID() {
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }
}
