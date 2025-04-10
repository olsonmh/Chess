import chess.*;

import com.google.gson.Gson;
import model.GameData;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;
import ui.Draw;

import java.net.URI;
import java.util.*;

import model.GameDataForListing;
import model.objects.CreateGameResult;
import model.objects.ListGamesResult;
import model.objects.LoginResult;
import ui.EscapeSequences;

import model.objects.RegisterResult;
import serverfacade.ServerFacade;
import ui.exceptions.FailException;
import ui.exceptions.QuitException;

import javax.websocket.*;

public class Main extends Endpoint {
    private final static ServerFacade SERVER_FACADE = new ServerFacade();
    private static String username;
    private static String authToken;
    private static String playerColor;
    private static boolean inGame = false;
    private static ChessGame currentGame = new ChessGame();


    public Gson serializer = new Gson();
    public int currentGameID;
    public Session session;

    Queue<String> messageQueue = new LinkedList<>();
    boolean isDrawing = false;

    public static void main(String[] args){
        new Main().run(args);
    }
    public Main(){
        session = null;
    }

    public void run(String[] args) {
        System.out.print("Welcome to chess\n");
        System.out.print("Type help for more information\n");
        try {
            while (true) { //it will throw an exception
                linePrint();
                Scanner scanner = new Scanner(System.in);
                String line = scanner.nextLine();
                try {
                    if (authToken == null) {
                        preLoginCommands(line);
                    } else {
                        if (!inGame){
                            postLoginCommands(line);
                        } else{
                            gameUiCommands(line);
                        }

                    }
                } catch (QuitException e){
                    throw e;
                } catch (Exception e) {
                    System.out.print(e.getMessage()+ "\n");
                }
            }
        } catch (QuitException e) {
            System.out.print("quiting");
        }
    }
    public static void linePrint(){
        if (authToken == null){
            System.out.print(">>> ");
        } else {
            if (!inGame) {
                System.out.print(EscapeSequences.SET_TEXT_COLOR_WHITE + "[%s] >>> ".formatted(username) + EscapeSequences.RESET_TEXT_COLOR);
            } else {
                if (playerColor == null){
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_YELLOW + "[%s] >>> ".formatted(username) + EscapeSequences.RESET_TEXT_COLOR);
                }
                else if (playerColor.equals("BLACK")){
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_RED + "[%s] >>> ".formatted(username) + EscapeSequences.RESET_TEXT_COLOR);
                } else {
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + "[%s] >>> ".formatted(username) + EscapeSequences.RESET_TEXT_COLOR);
                }
            }
        }
    }
    public void preLoginCommands(String line){
        var tokens = line.split(" ");
        switch(tokens[0].toLowerCase()){
            case "help":
                System.out.print("register <Username> <Password> <Email> - to register a user\n");
                System.out.print("login <Username> <Password> - to login existing user\n");
                System.out.print("quit - to exit program\n");
                break;
            case "exit":
            case "quit":
                throw new QuitException("quitting");
            case "login":
                try {
                    LoginResult loginResult = SERVER_FACADE.login(tokens[1], tokens[2]);
                    username = loginResult.username();
                    authToken = loginResult.authToken();
                } catch (Exception e) {
                    throw new FailException("""
                                            Incorrect password or username.
                                             Try 'help' for commands.
                                            """);
                }
                break;
            case "register":
                try {
                    RegisterResult result = SERVER_FACADE.register(tokens[1], tokens[2], tokens[3]);
                    username = result.username();
                    authToken = result.authToken();
                } catch (Exception e) {
                    throw new FailException("""
                            Could not register user. \
                            User may already exist or something was typed wrong.
                             Try 'help' for commands.
                            """);
                }
                break;
            case "logout":
            case "create":
            case "list":
            case "play":
            case "observe":
                System.out.print("You must be registered and logged in before you can execute that command\n");
                break;
            default:
                System.out.print("You typed something wrong\n");
        }
    }

    public void postLoginCommands(String line){
        var tokens = line.split(" ");
        //String playerColor;
        switch(tokens[0].toLowerCase()){
            case "help":
                System.out.print("logout - to logout current user\n");
                System.out.print("create <name> - create a game with specified name\n");
                System.out.print("list - list all current existing games\n");
                System.out.print("play <ID> [BLACK|WHITE] - join and play a game\n");
                System.out.print("observe <ID> - observe existing game\n");
                System.out.print("quit - to exit program\n");
                break;
            case "exit":
            case "quit":
                throw new QuitException("quitting");
            case "logout":
                SERVER_FACADE.logout(authToken);
                authToken = null;
                username = null;
                break;
            case "create":
                try {
                    CreateGameResult gameResult = SERVER_FACADE.createGame(authToken, tokens[1]);
                    System.out.printf("Game %s created with ID: %d\n", tokens[1], gameResult.gameID());
                } catch (Exception e) {
                    throw new FailException("""
                                            Could not create game.
                                             Try 'help' for commands.
                                            """);
                }
                break;
            case "list":
                ListGamesResult listGamesResult = SERVER_FACADE.listGames(authToken);
                System.out.println("List of Games:");
                System.out.println("--------------------------------------");
                for (GameDataForListing game : listGamesResult.games()) {
                    System.out.printf("Game ID: %d\n", game.gameID());
                    System.out.printf("Game Name: %s\n", game.gameName());
                    System.out.printf("White Player: %s\n", game.whiteUsername());
                    System.out.printf("Black Player: %s\n", game.blackUsername());
                    System.out.println("--------------------------------------");
                }
                break;
            case "join":
            case "play":
                try {
                    try {
                        currentGameID = Integer.parseInt(tokens[1]);
                    } catch (NumberFormatException e) {
                        throw new FailException("Error, game id not valid\n");
                    }
                    playerColor = tokens[2].toUpperCase();
                    SERVER_FACADE.joinGame(authToken, playerColor, currentGameID);
                } catch (FailException e){
                    throw e;
                } catch (Exception e) {
                    throw new FailException("Could not join game. Game may not exist.\n");
                }

                UserGameCommand connectCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, currentGameID);
                executeCommand(connectCommand);
                inGame = true;
                break;
            case "observe":
                if (tokens.length < 2){
                    throw new FailException("Error, no game id given.\n");
                }
                try {
                    currentGameID = Integer.parseInt(tokens[1]);
                } catch (NumberFormatException e) {
                    throw new FailException("Error, game id not valid\n");
                }
                inGame = true;
                UserGameCommand observeCommand = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, currentGameID);
                executeCommand(observeCommand);
                break;
            case "login":
                System.out.print("A user is already logged in\n");
                break;
            case "register":
                System.out.print("A user is already logged in. You should log out before registering a new user.\n");
                break;
            default:
                System.out.print("You typed something wrong\n");
        }
    }

    public void gameUiCommands(String line){
        var tokens = line.split(" ");
        switch(tokens[0].toLowerCase()){
            case "help":
                System.out.print("highlight <piece location> - highlights legal moves for piece e.g. b2\n");
                System.out.print("resign - forfeit game\n");
                System.out.print("move <start location> <end location> - move piece e.g. b2 b4\n");
                System.out.print("draw - redraw chessboard\n");
                System.out.print("leave - to leave current game\n");
                break;
            case "draw":
                if (playerColor == null){
                    Draw.drawBoard(currentGame, "WHITE", null);
                } else{
                    Draw.drawBoard(currentGame, playerColor, null);
                }

                break;
            case "leave":
                UserGameCommand leaveCommand = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, currentGameID);
                executeCommand(leaveCommand);

                inGame = false;
                playerColor = null;
                break;
            case "resign":
                UserGameCommand resignCommand = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, currentGameID);
                executeCommand(resignCommand);
                break;
            case "h":
            case "highlight":
                String piece = tokens[1];
                Draw.drawBoard(currentGame, playerColor, piece);
                break;
            case "m":
            case "move":
                ChessPosition startPose = Draw.getPose(tokens[1], playerColor);
                ChessPosition endPose = Draw.getPose(tokens[2], playerColor);
                ChessMove move = new ChessMove(startPose, endPose, null);

                UserGameCommand moveCommand = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, authToken, currentGameID, move);
                executeCommand(moveCommand);
                break;
            default:
                System.out.print("You typed something wrong\n");
        }
    }

    private void executeCommand(UserGameCommand command){
        try {
            URI uri = new URI("ws://localhost:"+ SERVER_FACADE.getPort() + "/ws");

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, uri);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                public void onMessage(String message) {
                    messageQueue.offer(message);
                    processNextMessage();
                }
            });

            String json = serializer.toJson(command);
            this.session.getBasicRemote().sendText(json);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void loadGame(String json){
        GameData gameData = serializer.fromJson(json, GameData.class);

        startDraw();
        Draw.drawBoard(gameData.game(), playerColor, null);
        linePrint();
        endDraw();

        currentGame = gameData.game();
    }

    private void printNotification(String message){
        System.out.print(message);
        linePrint();
    }

    private void printError(String message){
        System.out.print(message);
        if (message.equals("\nGame not found.\n")){
            inGame = false;
        }
        linePrint();

    }

    private void processNextMessage() {

        while (!isDrawing && !messageQueue.isEmpty()) {
            String message = messageQueue.poll();

            ServerMessage serverMessage = serializer.fromJson(message, ServerMessage.class);
            switch(serverMessage.getServerMessageType()){
                case LOAD_GAME -> loadGame(serverMessage.game);
                case NOTIFICATION -> printNotification(serverMessage.message);
                case ERROR -> printError(serverMessage.errorMessage);
            }
        }
    }

    public void startDraw() {
        isDrawing = true;
    }

    public void endDraw() {
        isDrawing = false;
        processNextMessage();
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}