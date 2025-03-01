package service;
import model.*;

import org.junit.jupiter.api.*;

import service.exceptions.*;
import service.requestResult.*;

import java.util.Objects;


public class ClearServiceTest {
    private UserService userService;
    private GameService gameService;
    private ClearService clearService;

    @BeforeEach
    public void setup() {
        gameService = new GameService();
        userService = new UserService();
        clearService = new ClearService();
    }

    @Test
    @DisplayName("Positive Clear Test")
    public void positiveClearTest() {
        RegisterRequest registerRequest = new RegisterRequest("Micah", "hello", "micah@email.com");
        RegisterResult registerResult = userService.register(registerRequest);
        RegisterRequest registerRequest2 = new RegisterRequest("greg", "whatNow", "greg@email.com");
        userService.register(registerRequest2);

        CreateGameRequest createGameRequest = new CreateGameRequest(registerResult.authToken(), "NewGame");
        gameService.createGame(createGameRequest);

        clearService.clearAll();

        assert Service.userData.getUser("Micah") == null;
        assert Service.gameData.listGames().isEmpty();
        assert Service.authData.getAuth(registerResult.authToken()) == null;
    }
}
