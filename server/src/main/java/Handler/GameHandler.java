package Handler;

import com.google.gson.Gson;
import service.UserService;
import service.exceptions.AuthTokenNotFoundException;
import service.exceptions.GameNotFoundException;
import service.requestResult.*;
import service.GameService;

public class GameHandler {
    private final Gson serializer;
    private final GameService service = new GameService();

    public GameHandler(){
        this.serializer = new Gson();
    }

    public String listAllGames(String json) throws AuthTokenNotFoundException{
        ListGamesRequest listGamesRequest = serializer.fromJson(json, ListGamesRequest.class);
        ListGamesResult listGamesResult = service.listGames(listGamesRequest);
        return serializer.toJson(listGamesResult);
    }

    public String createNewGame(String json) throws AuthTokenNotFoundException{
        CreateGameRequest createGameRequest = serializer.fromJson(json, CreateGameRequest.class);
        CreateGameResult createGameResult = service.createGame(createGameRequest);
        return serializer.toJson(createGameResult);
    }

    public void joinNewGame(String json) throws GameNotFoundException, AuthTokenNotFoundException {
        JoinGameRequest joinGameRequest = serializer.fromJson(json, JoinGameRequest.class);
        service.joinGame(joinGameRequest);
    }
}
