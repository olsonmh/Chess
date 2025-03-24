package client;

import org.junit.jupiter.api.*;
import server.Server;
import serverfacade.ServerFacade;
import model.objects.*;

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

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    @DisplayName("Positive Register Test")
    public void positiveRegisterTest(){
        myServerFacade.clearAll();
        RegisterResult result = myServerFacade.register("micah", "password", "email.com");
        assert(result.username().equals("micah"));
        assert(result.authToken() != null);
    }

    @Test
    @DisplayName("Negative Register Test")
    public void negativeRegisterTest() {
        myServerFacade.clearAll();
        assertThrows(Exception.class, () -> myServerFacade.register("micah", null, null));
    }

    @Test
    @DisplayName("Positive Create Game Test")
    public void positiveCreateGameTest(){
        myServerFacade.clearAll();
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
        myServerFacade.clearAll();
        RegisterResult result = myServerFacade.register("micah", "password", "email.com");
        String authToken = result.authToken();

        assertDoesNotThrow(() -> myServerFacade.createGame(authToken, "myGame"));
        assertThrows(Exception.class, () -> myServerFacade.createGame(authToken, null));
    }

    @Test
    @DisplayName("Positive Logout Test")
    public void positiveLogoutTest(){
        myServerFacade.clearAll();
        RegisterResult result = myServerFacade.register("micah", "password", "email.com");
        String authToken = result.authToken();
        assertDoesNotThrow(() -> myServerFacade.logout(authToken));

        assertThrows(Exception.class, () -> myServerFacade.createGame(authToken, "myGame"));
    }

    @Test
    @DisplayName("Negative Logout Test")
    public void negativeLogoutTest(){
        myServerFacade.clearAll();
        String authToken = "w9dj9djwd10d1wd";
        assertThrows(Exception.class, () -> myServerFacade.logout(authToken));
    }



}
