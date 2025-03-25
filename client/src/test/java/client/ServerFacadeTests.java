package client;

import model.GameDataForListing;
import org.junit.jupiter.api.*;
import server.Server;
import serverfacade.ServerFacade;
import model.objects.*;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class ServerFacadeTests {

    private static Server server;
    private final ServerFacade myServerFacade = new ServerFacade();

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(8080);
        System.out.println("Started test HTTP server on " + port);
    }

    @BeforeEach
    public void setup() {
        ServerFacade.clearAll();
    }


    @AfterAll
    static void stopServer() {
        ServerFacade.clearAll();
        server.stop();
    }

    @Test
    @DisplayName("Positive Register Test")
    public void positiveRegisterTest(){
        RegisterResult result = myServerFacade.register("micah", "password", "email.com");
        assert(result.username().equals("micah"));
        assert(result.authToken() != null);
    }

    @Test
    @DisplayName("Negative Register Test")
    public void negativeRegisterTest() {
        assertThrows(Exception.class, () -> myServerFacade.register("micah", null, null));
    }

    @Test
    @DisplayName("Positive Create Game Test")
    public void positiveCreateGameTest(){
        RegisterResult result = myServerFacade.register("micah", "password", "email.com");
        String authToken = result.authToken();

        try{
            CreateGameResult gameResult = myServerFacade.createGame(authToken, "myGame");
            assert(gameResult.gameID() != 0);
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Negative Create Game Test")
    public void negativeCreateGameTest(){
        RegisterResult result = myServerFacade.register("micah", "password", "email.com");
        String authToken = result.authToken();

        assertDoesNotThrow(() -> myServerFacade.createGame(authToken, "myGame"));
        assertThrows(Exception.class, () -> myServerFacade.createGame(authToken, null));
    }

    @Test
    @DisplayName("Positive Logout Test")
    public void positiveLogoutTest(){
        RegisterResult result = myServerFacade.register("micah", "password", "email.com");
        String authToken = result.authToken();
        assertDoesNotThrow(() -> myServerFacade.logout(authToken));

        assertThrows(Exception.class, () -> myServerFacade.createGame(authToken, "myGame"));
    }

    @Test
    @DisplayName("Negative Logout Test")
    public void negativeLogoutTest(){
        String authToken = "w9dj9djwd10d1wd";
        assertThrows(Exception.class, () -> myServerFacade.logout(authToken));
    }

    @Test
    @DisplayName("Positive Login Test")
    public void positiveLoginTest(){
        RegisterResult result = myServerFacade.register("micah", "password", "email.com");
        assertDoesNotThrow(() -> myServerFacade.logout(result.authToken()));
        try{
            LoginResult loginResult  = myServerFacade.login("micah", "password");
            assert(loginResult.username().equals("micah"));
            assert(loginResult.authToken() != null);
        } catch (Exception e){
            fail(e);
        }
    }

    @Test
    @DisplayName("Negative Login Test")
    public void negativeLoginTest(){
        assertThrows(Exception.class, () -> myServerFacade.login("micah", "password"));
    }

    @Test
    @DisplayName("Positive Join Game Test")
    public void positiveJoinGameTest(){
        RegisterResult result = myServerFacade.register("Greg", "newPass", "greg@email.com");
        CreateGameResult gameResult = myServerFacade.createGame(result.authToken(), "ThisGameIsTheBest");

        String authToken = result.authToken();
        int gameID = gameResult.gameID();
        String playerColor = "WHITE";

        try{
            String response = myServerFacade.joinGame(authToken,playerColor,gameID);
            assert(response.equals("{}"));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Negative Join Game Test")
    public void negativeJoinGameTest(){
        RegisterResult result = myServerFacade.register("Greg", "newPass", "greg@email.com");
        CreateGameResult gameResult = myServerFacade.createGame(result.authToken(), "ThisGameIsTheBest");

        String authToken = result.authToken();
        int gameID = gameResult.gameID();
        String playerColor = "WHITE";

        assertThrows(Exception.class, () -> myServerFacade.joinGame(authToken, playerColor, 123456));
        assertDoesNotThrow(() -> myServerFacade.joinGame(authToken,playerColor,gameID));
        assertThrows(Exception.class, () -> myServerFacade.joinGame(authToken,playerColor,gameID));
    }

    @Test
    @DisplayName("Positive List Games Test")
    public void positiveListGamesTest(){
        RegisterResult result = myServerFacade.register("Greg", "newPass", "greg@email.com");
        RegisterResult result2 = myServerFacade.register("micah", "password", "micah@email.com");
        CreateGameResult gameResult = myServerFacade.createGame(result.authToken(), "WhatIsMyName");
        myServerFacade.createGame(result.authToken(), "dumbGame");

        int gameID = gameResult.gameID();
        String playerColor = "WHITE";
        String playerColor2 = "BLACK";

        myServerFacade.joinGame(result.authToken(),playerColor,gameID);
        myServerFacade.joinGame(result2.authToken(),playerColor2,gameID);

        try{
            ListGamesResult listGames = myServerFacade.listGames(result.authToken());
            List<String> expectedGameNames = List.of("dumbGame", "WhatIsMyName");
            List<String> actualGameNames = listGames.games().stream().map(GameDataForListing::gameName).toList();
            assertTrue(actualGameNames.containsAll(expectedGameNames));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Negative List Games Test")
    public void negativeListGamesTest(){
        String authToken = "wi3nd19ddniqnfiqjd";
        assertThrows(Exception.class, () -> myServerFacade.listGames(authToken));
    }

    @Test
    @DisplayName("Positive Clear All Test")
    public void negativeClearAllTest(){
        myServerFacade.register("Greg", "newPass", "greg@email.com");
        String message = ServerFacade.clearAll();
        assertDoesNotThrow(() -> myServerFacade.register("Greg", "newPass", "greg@email.com"));
        assert(message.equals("{}"));
    }
}
