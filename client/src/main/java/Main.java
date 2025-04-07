import chess.*;
import java.util.Scanner;

import model.GameDataForListing;
import model.objects.CreateGameResult;
import model.objects.ListGamesResult;
import model.objects.LoginResult;
import ui.EscapeSequences;

import model.objects.RegisterResult;
import serverfacade.ServerFacade;
import ui.exceptions.FailException;
import ui.exceptions.QuitException;

public class Main {
    private final static ServerFacade SERVER_FACADE = new ServerFacade();
    private static String username;
    private static String authToken;
    private static final ChessGame currentGame = new ChessGame();

    public static void main(String[] args) {
        System.out.print("Welcome to chess\n");
        System.out.print("Type help for more information\n");
        try {
            while (true) { //it will throw an exception
                if (authToken == null){
                    System.out.print(">>> ");
                } else {
                    System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + "[%s] >>> ".formatted(username) + EscapeSequences.RESET_TEXT_COLOR);
                }
                Scanner scanner = new Scanner(System.in);
                String line = scanner.nextLine();
                try {
                    if (authToken == null) {
                        preLoginCommands(line);
                    } else {
                        postLoginCommands(line);
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

    public static void preLoginCommands(String line){
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

    public static void postLoginCommands(String line){
        var tokens = line.split(" ");
        String playerColor;
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
                    SERVER_FACADE.joinGame(authToken, tokens[2].toUpperCase(), Integer.parseInt(tokens[1]));
                    playerColor = tokens[2].toUpperCase();
                    System.out.printf("User %s has joined %d\n", username, Integer.parseInt(tokens[1]));
                } catch (Exception e) {
                    throw new FailException("Could not join game. Game may not exist.\n");
                }
                drawBoard(currentGame, playerColor);
                break;
            case "observe":
                drawBoard(currentGame, "WHITE");
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

    public static void drawBoard(ChessGame game, String color){
        String backgroundColor = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
        boolean flipBoard = color.equals("WHITE");

        System.out.printf("%s   ", backgroundColor);
        if (flipBoard) {
            for (char ch = 'a'; ch <= 'h'; ch++) {
                System.out.printf("%s %s ", backgroundColor, ch);
            }
        } else{
            for (char ch = 'h'; ch >= 'a'; ch--) {
                System.out.printf("%s %s ", backgroundColor, ch);
            }
        }
        System.out.printf("%s   " + EscapeSequences.RESET_BG_COLOR + "\n", backgroundColor);

        for(int i = 0; i<=7; i++){
            int row = flipBoard ? 7 - i : i;
            System.out.printf("%s %d ", EscapeSequences.SET_BG_COLOR_LIGHT_GREY, row+1);

            for(int j = 0; j<=7; j++){

                backgroundColor = (i + j) % 2 == 0 ? EscapeSequences.SET_BG_COLOR_WHITE : EscapeSequences.SET_BG_COLOR_BLACK;

                String label;
                ChessPiece piece = game.getBoard().board[row][j];
                if (piece == null) {
                    label = "   ";
                } else {
                    label = switch (piece.getPieceType()) {
                        case PAWN -> EscapeSequences.BLACK_PAWN;
                        case ROOK -> EscapeSequences.BLACK_ROOK;
                        case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
                        case QUEEN -> EscapeSequences.BLACK_QUEEN;
                        case KING -> EscapeSequences.BLACK_KING;
                        case BISHOP -> EscapeSequences.BLACK_BISHOP;
                    };
                    if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                        label = EscapeSequences.SET_TEXT_COLOR_BLUE + label + EscapeSequences.RESET_TEXT_COLOR;
                    } else {
                        label = EscapeSequences.SET_TEXT_COLOR_RED + label + EscapeSequences.RESET_TEXT_COLOR;
                    }
                }
                System.out.printf("%s%s", backgroundColor, label);
            }
            System.out.printf("%s %d ", EscapeSequences.SET_BG_COLOR_LIGHT_GREY, row+1);
            System.out.print(EscapeSequences.RESET_BG_COLOR + "\n");
        }

        backgroundColor = EscapeSequences.SET_BG_COLOR_LIGHT_GREY;
        System.out.printf("%s   ", backgroundColor);
        if (flipBoard){
            for (char ch = 'a'; ch <= 'h'; ch++) {
                System.out.printf("%s %s ", backgroundColor, ch);
            }
        } else {
            for (char ch = 'h'; ch >= 'a'; ch--) {
                System.out.printf("%s %s ", backgroundColor, ch);
            }
        }
        System.out.printf("%s   " + EscapeSequences.RESET_BG_COLOR + "\n", backgroundColor);

    }

}