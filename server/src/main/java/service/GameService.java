package service;

import chess.ChessGame;
import service.objects.*;
import model.*;
import service.exceptions.*;

import java.util.Collection;


public class GameService extends Service{

    public ListGamesResult listGames(ListGamesRequest listGamesRequest) {
        AuthData auth = authData.getAuth(listGamesRequest.authToken());
        if(auth != null){
            Collection<GameDataForListing> listOfGames = gameData.listGames();
            return new ListGamesResult(listOfGames);
        }
        throw new AuthTokenNotFoundException("Not logged in");

    }

    public CreateGameResult createGame(CreateGameRequest createGameRequest) {
        AuthData auth = authData.getAuth(createGameRequest.authToken());
        if(auth == null){
            throw new AuthTokenNotFoundException("Not logged in");
        }

        int gameID = generateGameID();
        ChessGame game = new ChessGame();
        gameData.createGame(new GameData(gameID, null, null, createGameRequest.gameName(), game));
        return new CreateGameResult(gameID);

    }


    public void joinGame(JoinGameRequest joinGameRequest) {
        AuthData auth = authData.getAuth(joinGameRequest.authToken());
        if (auth == null) {
            throw new AuthTokenNotFoundException("Not logged in");
        }

        GameData game = gameData.getGame(joinGameRequest.gameID());

        if (game == null) {
            throw new GameNotFoundException("Game does not exist.");
        }

        switch (joinGameRequest.playerColor()) {
            case "BLACK":
                if (game.blackUsername() == null) {
                    gameData.updateGame(new GameData(game.gameID(),
                                                     game.whiteUsername(),
                                                     auth.username(),
                                                     game.gameName(),
                                                     game.game()));
                    return;
                }
                throw new PlayerAlreadyTakenException("Player color is already being used.");
            case "WHITE":
                if (game.whiteUsername() == null) {
                    gameData.updateGame(new GameData(game.gameID(),
                                                     auth.username(),
                                                     game.blackUsername(),
                                                     game.gameName(),
                                                     game.game()));
                    return;
                }
                throw new PlayerAlreadyTakenException("Player color is already being used.");
            case null:
            default:
                throw new WrongColorException("Color does not exist.");
        }
    }

}
