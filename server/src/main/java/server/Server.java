package server;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import model.GameData;
import service.exceptions.*;
import spark.*;
import handler.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.util.HashMap;
import java.util.Map;


@WebSocket
public class Server {

    private final UserHandler userHandler = new UserHandler();
    private final GameHandler gameHandler = new GameHandler();
    private final ClearHandler clearHandler = new ClearHandler();
    Gson serializer = new Gson();

    Map<Integer, Map<String, org.eclipse.jetty.websocket.api.Session>> clientSessions = new HashMap<>();
    //Map<String, org.eclipse.jetty.websocket.api.Session> clientSessions = new HashMap<>();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.webSocket("/ws", Server.class);
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);
        Spark.get("/game", this::listAllGames);
        Spark.post("/game", this::createNewGame);
        Spark.put("/game", this::joinNewGame);
        Spark.delete("/db", this::clear);


        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object registerUser(Request req, Response res) {
        try {
            String json = userHandler.registerUser(req.body());
            res.type("application/json");
            return json;
        } catch (UserExistException e) {
            res.type("application/json");
            res.status(403);
            String error = "{ \"message\": \"Error: User already taken\" }";
            res.body(error);
            return error;
        } catch (BadRegisterRequestException e) {
            res.type("application/json");
            res.status(400);
            String error = "{ \"message\": \"Error: bad request\" }";
            res.body(error);
            return error;
        }

    }

    private Object loginUser(Request req, Response res) {
        try {
            String json = userHandler.loginUser(req.body());
            res.type("application/json");
            return json;
        } catch (WrongPasswordException e) {
            res.type("application/json");
            res.status(401);
            String error = "{ \"message\": \"Error: unauthorized\" }";
            res.body(error);
            return error;
        } catch (UserNotFoundException e) {
            res.type("application/json");
            res.status(401);
            String error = "{ \"message\": \"Error: user not found\" }";
            res.body(error);
            return error;
        }
    }

    private Object logoutUser(Request req, Response res) {
        try {
            userHandler.logoutUser(req.headers("authorization"));
            return "{}";
        } catch (AuthTokenNotFoundException e) {
            res.type("application/json");
            res.status(401);
            String error = "{ \"message\": \"Error: unauthorized\" }";
            res.body(error);
            return error;
        }
    }

    private Object listAllGames(Request req, Response res) {
        try {
            String json = gameHandler.listAllGames(req.headers("authorization"));
            res.type("application/json");
            return json;
        } catch (AuthTokenNotFoundException e) {
            res.type("application/json");
            res.status(401);
            String error = "{ \"message\": \"Error: unauthorized\" }";
            res.body(error);
            return error;
        }
    }

    private Object createNewGame(Request req, Response res) {
        try {
            String json = gameHandler.createNewGame(req.headers("authorization"), req.body());
            res.type("application/json");
            return json;
        } catch (AuthTokenNotFoundException e) {
            res.type("application/json");
            res.status(401);
            String error = "{ \"message\": \"Error: unauthorized\" }";
            res.body(error);
            return error;
        }
    }

    private Object joinNewGame(Request req, Response res) {
        try {
            gameHandler.joinNewGame(req.headers("authorization"), req.body());
            return "{}";
        } catch (AuthTokenNotFoundException e) {
            res.type("application/json");
            res.status(401);
            String error = "{ \"message\": \"Error: unauthorized\" }";
            res.body(error);
            return error;
        } catch (GameNotFoundException e) {
            res.type("application/json");
            res.status(400);
            String error = "{ \"message\": \"Error: bad request game not found\" }";
            res.body(error);
            return error;
        } catch (PlayerAlreadyTakenException e) {
            res.type("application/json");
            res.status(403);
            String error = "{ \"message\": \"Error: player color already in use.\" }";
            res.body(error);
            return error;
        } catch (WrongColorException e) {
            res.type("application/json");
            res.status(400);
            String error = "{ \"message\": \"Error: team color does not exist\" }";
            res.body(error);
            return error;
        }
    }

    private Object clear(Request req, Response res) {
        try {
            clearHandler.clear();
            res.type("application/json");
            return "{}";
        } catch (Exception e) {
            res.type("application/json");
            res.status(500);
            String error = "{ \"message\": \"Error: unknown error\" }";
            res.body(error);
            return error;
        }
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private void connect(org.eclipse.jetty.websocket.api.Session session, String username, UserGameCommand command) throws Exception{
        GameData game = gameHandler.getGame(command.getGameID());

        if (game == null){
            String mesg = "\nGame not found.\n";
            ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, mesg);
            String jsonNotification = serializer.toJson(notification);
            session.getRemote().sendString(jsonNotification);
        } else {

            String json = serializer.toJson(game);
            ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, json);
            String jsonMessage = serializer.toJson(loadGameMessage);

            var gameSessionMap = clientSessions.get(command.getGameID());
            for (Map.Entry<String, org.eclipse.jetty.websocket.api.Session> entry : gameSessionMap.entrySet()) {
                org.eclipse.jetty.websocket.api.Session storedSession = entry.getValue();
                String mesg;
                if (username.equals(game.whiteUsername())) {
                    mesg = String.format("\n%s has joined game %d as white player\n", username, command.getGameID());

                } else if (username.equals(game.blackUsername())) {
                    mesg = String.format("\n%s has joined game %d as black player\n", username, command.getGameID());
                } else {
                    mesg = String.format("\n%s has joined game %d as observer\n", username, command.getGameID());
                }
                ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, mesg);
                String jsonNotification = serializer.toJson(notification);
                if (!storedSession.equals(session)) {
                    storedSession.getRemote().sendString(jsonNotification);
                }
            }


            session.getRemote().sendString(jsonMessage);
        }
    }

    private void leaveGame(org.eclipse.jetty.websocket.api.Session session, String username, UserGameCommand command) throws Exception{
        GameData game = gameHandler.getGame(command.getGameID());

        if (username.equals(game.whiteUsername())){
            GameData updatedGame = new GameData(game.gameID(), null, game.blackUsername(), game.gameName(), game.game());
            gameHandler.updateGame(updatedGame);

        } else if (username.equals(game.blackUsername())){
            GameData updatedGame = new GameData(game.gameID(), game.whiteUsername(), null, game.gameName(), game.game());
            gameHandler.updateGame(updatedGame);
        }

        var gameSessionMap = clientSessions.get(command.getGameID());
        for (Map.Entry<String, org.eclipse.jetty.websocket.api.Session> entry : gameSessionMap.entrySet()) {
            org.eclipse.jetty.websocket.api.Session storedSession = entry.getValue();
            String mesg;
            if (username.equals(game.whiteUsername())) {
                mesg = String.format("\n%s has left game %d\n", username, command.getGameID());

            } else if (username.equals(game.blackUsername())) {
                mesg = String.format("\n%s has left game %d\n", username, command.getGameID());
            } else {
                mesg = String.format("\n%s has left game %d\n", username, command.getGameID());
            }
            ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, mesg);
            String jsonNotification = serializer.toJson(notification);
            if (!session.equals(storedSession)) {
                storedSession.getRemote().sendString(jsonNotification);
            }
        }

        clientSessions.get(command.getGameID()).remove(username);
    }

    private void resign(org.eclipse.jetty.websocket.api.Session session, String username, UserGameCommand command) throws Exception{
        GameData game = gameHandler.getGame(command.getGameID());
        ChessGame chessGame = game.game();

        if (chessGame.getWinner() != null){
            String errorMessage = "\nGame is over you cannot resign.\n";
            ServerMessage errorNotification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, errorMessage);
            String jsonError = serializer.toJson(errorNotification);
            session.getRemote().sendString(jsonError);
            return;
        }

        if (username.equals(game.whiteUsername())){
            if (game.blackUsername() == null){
                chessGame.setWinner("No Winner");
            } else {
                chessGame.setWinner(game.blackUsername());
            }
        } else if (username.equals(game.blackUsername())){
            if (game.whiteUsername() == null){
                chessGame.setWinner("No Winner");
            } else {
                chessGame.setWinner(game.whiteUsername());
            }
        } else {
            String errorMessage = "\nYou are not able to resign.\n";
            ServerMessage errorNotification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, errorMessage);
            String jsonError = serializer.toJson(errorNotification);
            session.getRemote().sendString(jsonError);
            return;
        }
        GameData updatedGame = new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), chessGame);
        gameHandler.updateGame(updatedGame);

        var gameSessionMap = clientSessions.get(command.getGameID());
        for (Map.Entry<String, org.eclipse.jetty.websocket.api.Session> entry : gameSessionMap.entrySet()) {
            org.eclipse.jetty.websocket.api.Session storedSession = entry.getValue();

            String message = String.format("\n%s has resigned\n", username);
            ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
            String jsonNotification = serializer.toJson(notification);
            storedSession.getRemote().sendString(jsonNotification);
        }
    }

    private void makeMove(org.eclipse.jetty.websocket.api.Session session, String username, UserGameCommand command) throws Exception{
        GameData game = gameHandler.getGame(command.getGameID());
        if (game.game().getWinner() != null){
            String errorMessage = "\nGame is over bud.\n";
            ServerMessage errorNotification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, errorMessage);
            String jsonError = serializer.toJson(errorNotification);
            session.getRemote().sendString(jsonError);
            return;
        }
        ChessMove move = command.getMove();

        if (username.equals(game.whiteUsername())){
            if (!game.game().getBoard().getPiece(move.getStartPosition()).getTeamColor().equals(ChessGame.TeamColor.WHITE)){
                sendUnauthorizedPieceMoveError(session);
                return;
            }
        } else if (username.equals(game.blackUsername())){
            if (!game.game().getBoard().getPiece(move.getStartPosition()).getTeamColor().equals(ChessGame.TeamColor.BLACK)){
                sendUnauthorizedPieceMoveError(session);
                return;
            }
        } else {
            sendUnauthorizedPieceMoveError(session);
            return;
        }


        ChessGame chessGame = game.game();
        try {
            chessGame.makeMove(move);

        } catch (InvalidMoveException e) {
            String message = "\nInvalid move. It may not be your turn or invalid input was entered.\n";
            ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, message);
            String jsonNotification = serializer.toJson(notification);
            session.getRemote().sendString(jsonNotification);
        }

        GameData updatedGame = new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), chessGame);
        gameHandler.updateGame(updatedGame);

        String checkMate = isInCheck(updatedGame, username);
        if (checkMate != null){
            chessGame.setWinner(checkMate);
            GameData updateGameWinner = new GameData(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName(), chessGame);
            gameHandler.updateGame(updateGameWinner);
        }

        String json = serializer.toJson(updatedGame);
        ServerMessage loadGameMessage = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, json);
        String jsonMessage = serializer.toJson(loadGameMessage);

        var gameSessionMap = clientSessions.get(command.getGameID());
        for (Map.Entry<String, org.eclipse.jetty.websocket.api.Session> entry : gameSessionMap.entrySet()) {
            org.eclipse.jetty.websocket.api.Session storedSession = entry.getValue();
            String mesg;
            if (move.getPromotionPiece() == null) {
                mesg = String.format("\n%s had moved %s from %s%s to %s%S\n",
                        username,
                        updatedGame.game().getBoard().getPiece(move.getEndPosition()).getPieceType().name().toLowerCase(),
                        numberToLetter(move.getStartPosition().getColumn()),
                        move.getStartPosition().getRow(),
                        numberToLetter(move.getEndPosition().getColumn()),
                        move.getEndPosition().getRow());
            } else {
                mesg = String.format("\n%s had moved %s from %s%s to %s%S\nand has promoted piece to %s\n",
                        username,
                        updatedGame.game().getBoard().getPiece(move.getEndPosition()).getPieceType().name().toLowerCase(),
                        numberToLetter(move.getStartPosition().getColumn()),
                        move.getStartPosition().getRow(),
                        numberToLetter(move.getEndPosition().getColumn()),
                        move.getEndPosition().getRow(),
                        move.getPromotionPiece().toString());
            }
            ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, mesg);
            String jsonNotification = serializer.toJson(notification);
            if (!session.equals(storedSession)){
                storedSession.getRemote().sendString(jsonNotification);
            }
            storedSession.getRemote().sendString(jsonMessage);
        }
    }

    private String numberToLetter(int number){
        Map<Integer, String> letterToNumber = new HashMap<>();
        letterToNumber.put(1, "a");
        letterToNumber.put(2, "b");
        letterToNumber.put(3, "c");
        letterToNumber.put(4, "d");
        letterToNumber.put(5, "e");
        letterToNumber.put(6, "f");
        letterToNumber.put(7, "g");
        letterToNumber.put(8, "h");
        return letterToNumber.get(number);
    }

    private String isInCheck(GameData gameData, String username) throws Exception{
        boolean check = false;
        boolean checkMate = false;
        String checkedPlayer = null;
        String winner = null;

        if (username.equals(gameData.blackUsername())){
            if (gameData.game().isInCheck(ChessGame.TeamColor.WHITE)){
                if (gameData.game().isInCheckmate(ChessGame.TeamColor.WHITE)){
                    checkMate = true;
                    winner = gameData.blackUsername();
                } else {
                    check = true;
                }
                checkedPlayer = gameData.whiteUsername();
            }
        } else if (username.equals(gameData.whiteUsername())){
            if (gameData.game().isInCheck(ChessGame.TeamColor.BLACK)){
                if (gameData.game().isInCheckmate(ChessGame.TeamColor.BLACK)){
                    checkMate = true;
                    winner = gameData.whiteUsername();
                } else {
                    check = true;
                }
                checkedPlayer = gameData.blackUsername();
            }
        } else {
            if (gameData.game().isInStalemate(ChessGame.TeamColor.WHITE)){
                winner = "No Winner";
            } else if (gameData.game().isInStalemate(ChessGame.TeamColor.BLACK)){
                winner = "No Winner";
            }
        }
        if (check || checkMate) {
            var gameSessionMap = clientSessions.get(gameData.gameID());
            for (Map.Entry<String, org.eclipse.jetty.websocket.api.Session> entry : gameSessionMap.entrySet()) {
                org.eclipse.jetty.websocket.api.Session storedSession = entry.getValue();
                String message = null;
                if (checkMate){
                    message = String.format("\n%s is in checkmate.\n", checkedPlayer);
                } else {
                    message = String.format("\n%s is in check.\n", checkedPlayer);
                }

                ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
                String jsonNotification = serializer.toJson(notification);
                storedSession.getRemote().sendString(jsonNotification);
            }
        }
        return winner;
    }

    private void sendUnauthorizedPieceMoveError(org.eclipse.jetty.websocket.api.Session session) throws Exception{
        String errorMessage = "\nYou are not authorized to move that piece.\n";
        ServerMessage errorNotification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, errorMessage);
        String jsonError = serializer.toJson(errorNotification);
        session.getRemote().sendString(jsonError);
    }

    @OnWebSocketMessage
    public void onMessage(org.eclipse.jetty.websocket.api.Session session, String message) throws Exception {
        System.out.printf("Received: %s", message);
        System.out.flush();
        try{
            UserGameCommand command = serializer.fromJson(message, UserGameCommand.class);
            String auth = command.getAuthToken();
            String username = userHandler.getUsername(auth);

            if (!clientSessions.containsKey(command.getGameID())){
                clientSessions.put(command.getGameID(), new HashMap<>());
            }
            clientSessions.get(command.getGameID()).put(username, session);

            switch (command.getCommandType()) {
                case CONNECT -> connect(session, username, command);
                case MAKE_MOVE -> makeMove(session, username, command);
                case RESIGN -> resign(session, username, command);
                case LEAVE -> leaveGame(session, username, command);
            }

        } catch (AuthTokenNotFoundException e){
            String mesg = "\nAuthentication token not found.\n";
            ServerMessage notification = new ServerMessage(ServerMessage.ServerMessageType.ERROR, mesg);
            String jsonNotification = serializer.toJson(notification);
            session.getRemote().sendString(jsonNotification);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
