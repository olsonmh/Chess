package service;
import dataaccess.*;
import java.util.UUID;
import java.util.Random;

public abstract class Service {
    //protected static AuthDAO authData = new MemoryAuthDAO();
    //protected static GameDAO gameData = new MemoryGameDAO();
    //protected static UserDAO userData = new MemoryUserDAO();
    protected static AuthDAO authData = new MySqlDataAccess();
    protected static GameDAO gameData = (GameDAO)authData;
    protected static UserDAO userData = (UserDAO)gameData;



    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

    public static int generateGameID() {
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }
}
