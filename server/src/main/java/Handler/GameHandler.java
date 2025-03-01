package Handler;

import com.google.gson.Gson;
import service.UserService;
import service.exceptions.AuthTokenNotFoundException;
import service.exceptions.GameNotFoundException;
import service.exceptions.PlayerAlreadyTakenException;
import service.requestResult.*;
import service.GameService;

public class GameHandler {
    private final Gson serializer;
    private final GameService service = new GameService();

    public GameHandler(){
        this.serializer = new Gson();
    }

    public String listAllGames(String authToken) throws AuthTokenNotFoundException{
        ListGamesRequest listGamesRequest = new ListGamesRequest(authToken);
        ListGamesResult listGamesResult = service.listGames(listGamesRequest);
        return serializer.toJson(listGamesResult);
    }

    public String createNewGame(String authToken, String json) throws AuthTokenNotFoundException{
        CreateGameRequest createGameRequest = serializer.fromJson(json, CreateGameRequest.class);
        createGameRequest = new CreateGameRequest(authToken, createGameRequest.gameName());
        CreateGameResult createGameResult = service.createGame(createGameRequest);
        return serializer.toJson(createGameResult);
    }

    public void joinNewGame(String authToken, String json) throws GameNotFoundException, AuthTokenNotFoundException, PlayerAlreadyTakenException {
        JoinGameRequest joinGameRequest = serializer.fromJson(json, JoinGameRequest.class);
        joinGameRequest = new JoinGameRequest(authToken, joinGameRequest.playerColor(), joinGameRequest.gameID());
        service.joinGame(joinGameRequest);
    }
}
