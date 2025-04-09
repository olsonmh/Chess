package service;
import dataaccess.*;
import model.AuthData;
import model.GameData;

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

    public String getUsername(String authToken){
        AuthData auth = authData.getAuth(authToken);
        return auth.username();
    }

    public GameData getGame(int gameID){
        return gameData.getGame(gameID);
    }

    public static int generateGameID() {
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }
}
