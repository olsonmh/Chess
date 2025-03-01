package service;
import chess.ChessGame;
import model.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import service.exceptions.*;
import service.requestResult.*;

import java.util.List;
import java.util.Objects;



public class GameServiceTests {
    private GameService gameService;
    private UserService userService;

    @BeforeEach
    public void setup() {
        gameService = new GameService();
        userService = new UserService();
    }

    @AfterEach
    public void remove(){
        Service.gameData.clearGames();
        Service.userData.clearUserData();
    }

    @Test
    @DisplayName("Positive Create Game Test")
    public void positiveCreateGameTest() {
        RegisterRequest registerRequest = new RegisterRequest("Micah", "password", "micah@email.com");
        RegisterResult registerResult = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest(registerResult.authToken(), "myGame");
        CreateGameResult createGameResult = gameService.createGame(createGameRequest);

        assert createGameResult.gameID() != 0;

        GameData game = Service.gameData.getGame(createGameResult.gameID());
        ChessGame newGame = new ChessGame();

        assert game.blackUsername() == null;
        assert game.whiteUsername() == null;
        assert Objects.equals(game.gameName(), "myGame");
        assert Objects.equals(game.game(), newGame);
    }

    @Test
    @DisplayName("Negative Create Game Test")
    public void negativeCreateGameTest() {
        String authToken = Service.generateToken();
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, "myGame");

        assertThrows(AuthTokenNotFoundException.class, () -> {
            gameService.createGame(createGameRequest);
        });
    }

        @Test
        @DisplayName("Positive Join Game Test")
        public void positiveJoinGameTest() {
            RegisterRequest registerRequest = new RegisterRequest("Micah", "password", "micah@email.com");
            RegisterResult registerResult = userService.register(registerRequest);
            String authTokenPlayerOne = registerResult.authToken();

            RegisterRequest registerRequest2 = new RegisterRequest("Greg", "betterPassword", "greg@email.com");
            RegisterResult registerResult2 = userService.register(registerRequest2);
            String authTokenPlayerTwo = registerResult2.authToken();

            CreateGameRequest createGameRequest = new CreateGameRequest(registerResult.authToken(), "NewGame");
            CreateGameResult createGameResult = gameService.createGame(createGameRequest);
            int gameID = createGameResult.gameID();

            JoinGameRequest joinGameRequest = new JoinGameRequest(authTokenPlayerOne, "WHITE", gameID);
            gameService.joinGame(joinGameRequest);

            assert Objects.equals(Service.gameData.getGame(gameID).whiteUsername(), "Micah");

            JoinGameRequest joinGameRequest2 = new JoinGameRequest(authTokenPlayerTwo, "BLACK", gameID);
            gameService.joinGame(joinGameRequest2);

            assert Objects.equals(Service.gameData.getGame(gameID).whiteUsername(), "Micah");
            assert Objects.equals(Service.gameData.getGame(gameID).blackUsername(), "Greg");
        }

    @Test
    @DisplayName("Negative Join Game Test")
    public void NegativeJoinGameTest() {
        RegisterRequest registerRequest = new RegisterRequest("Micah", "password", "micah@email.com");
        RegisterResult registerResult = userService.register(registerRequest);
        String authToken = registerResult.authToken();
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, "WHITE", 123456);

        assertThrows(GameNotFoundException.class, () -> {
            gameService.joinGame(joinGameRequest);
        });

        CreateGameRequest createGameRequest = new CreateGameRequest(registerResult.authToken(), "NewGame");
        CreateGameResult createGameResult = gameService.createGame(createGameRequest);
        int gameID = createGameResult.gameID();

        JoinGameRequest joinGameRequest2 = new JoinGameRequest(authToken, "WHITE", gameID);

        LogoutRequest logoutRequest = new LogoutRequest(joinGameRequest2.authToken());
        userService.logout(logoutRequest);

        assertThrows(AuthTokenNotFoundException.class, () -> {
            gameService.joinGame(joinGameRequest2);
        });
    }

    @Test
    @DisplayName("Negative Join Game Test 2")
    public void negativeJoinGameTest2() {
        RegisterRequest registerRequest = new RegisterRequest("Micah", "password", "micah@email.com");
        RegisterResult registerResult = userService.register(registerRequest);
        String authTokenPlayerOne = registerResult.authToken();

        RegisterRequest registerRequest2 = new RegisterRequest("Greg", "betterPassword", "greg@email.com");
        RegisterResult registerResult2 = userService.register(registerRequest2);
        String authTokenPlayerTwo = registerResult2.authToken();

        CreateGameRequest createGameRequest = new CreateGameRequest(registerResult.authToken(), "NewGame");
        CreateGameResult createGameResult = gameService.createGame(createGameRequest);
        int gameID = createGameResult.gameID();

        JoinGameRequest joinGameRequest = new JoinGameRequest(authTokenPlayerOne, "WHITE", gameID);
        gameService.joinGame(joinGameRequest);

        assert Objects.equals(Service.gameData.getGame(gameID).whiteUsername(), "Micah");

        JoinGameRequest joinGameRequest2 = new JoinGameRequest(authTokenPlayerTwo, "WHITE", gameID);

        assertThrows(PlayerAlreadyTakenException.class, () -> {
            gameService.joinGame(joinGameRequest2);
        });
    }

    @Test
    @DisplayName("Positive List Games Test")
    public void positiveListGamesTest() {
        RegisterRequest registerRequest = new RegisterRequest("Micah", "password", "micah@email.com");
        RegisterResult registerResult = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest(registerResult.authToken(), "myGame");
        gameService.createGame(createGameRequest);

        CreateGameRequest createGameRequest2 = new CreateGameRequest(registerResult.authToken(), "mySecondGame");
        gameService.createGame(createGameRequest2);

        CreateGameRequest createGameRequest3 = new CreateGameRequest(registerResult.authToken(), "myThirdGame");
        gameService.createGame(createGameRequest3);

        ListGamesRequest listGamesRequest = new ListGamesRequest(registerResult.authToken());
        ListGamesResult listGamesResult = gameService.listGames(listGamesRequest);

        List<String> expectedGameNames = List.of("myGame", "mySecondGame", "myThirdGame");
        List<String> actualGameNames = listGamesResult.games().stream().map(GameDataForListing::gameName).toList();
        assertTrue(actualGameNames.containsAll(expectedGameNames));
    }

    @Test
    @DisplayName("Negative List Games Test")
    public void negativeListGamesTest() {
        RegisterRequest registerRequest = new RegisterRequest("Micah", "password", "micah@email.com");
        RegisterResult registerResult = userService.register(registerRequest);

        CreateGameRequest createGameRequest = new CreateGameRequest(registerResult.authToken(), "myGame");
        gameService.createGame(createGameRequest);

        CreateGameRequest createGameRequest2 = new CreateGameRequest(registerResult.authToken(), "mySecondGame");
        gameService.createGame(createGameRequest2);

        CreateGameRequest createGameRequest3 = new CreateGameRequest(registerResult.authToken(), "myThirdGame");
        gameService.createGame(createGameRequest3);

        LogoutRequest logoutRequest = new LogoutRequest(registerResult.authToken());
        userService.logout(logoutRequest);

        ListGamesRequest listGamesRequest = new ListGamesRequest(registerResult.authToken());
        assertThrows(AuthTokenNotFoundException.class, () -> {
            gameService.listGames(listGamesRequest);
        });
    }
}