package service;

import chess.ChessGame;
import service.requestResult.*;
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
        if(auth != null){
            int gameID = generateGameID();
            ChessGame game = new ChessGame();
            gameData.createGame(new GameData(gameID, null, null, createGameRequest.gameName(), game));
            return new CreateGameResult(gameID);
        }
        throw new AuthTokenNotFoundException("Not logged in");
    }


    public void joinGame(JoinGameRequest joinGameRequest) {
        AuthData auth = authData.getAuth(joinGameRequest.authToken());
        if(auth != null){
            GameData game = gameData.getGame(joinGameRequest.gameID());
            if(game != null){
                switch(joinGameRequest.playerColor()){
                    case "BLACK":
                        if(game.blackUsername() == null){
                            gameData.updateGame(new GameData(game.gameID(), game.whiteUsername(), auth.username(), game.gameName(), game.game()));
                            return;
                        }
                        throw new PlayerAlreadyTakenException("Player color is already being used.");
                    case "WHITE":
                        if(game.whiteUsername() == null){
                            gameData.updateGame(new GameData(game.gameID(), auth.username(), game.blackUsername(), game.gameName(), game.game()));
                            return;
                        }
                        throw new PlayerAlreadyTakenException("Player color is already being used.");
                    default:
                        throw new RuntimeException();
                }
            }
            throw new GameNotFoundException("Game does not exist.");
        }
        throw new AuthTokenNotFoundException("Not logged in");
    }
}
