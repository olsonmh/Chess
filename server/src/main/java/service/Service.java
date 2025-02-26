package service;
import dataaccess.*;
import java.util.UUID;
import java.util.Random;

public abstract class Service {
    public static AuthDAO authData;
    public static GameDAO gameData;
    public static UserDAO userData;

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }
    public static int generateGameID() {
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }
}
