import chess.*;

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

public class Main {
    private final static ServerFacade SERVER_FACADE = new ServerFacade();
    private static String username;
    private static String authToken;
    private static String playerColor;
    private static String inGame;
    private static final ChessGame currentGame = new ChessGame();

    public static void main(String[] args) {
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
                        if (inGame == null){
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
    private static void linePrint(){
        if (authToken == null){
            System.out.print(">>> ");
        } else {
            if (inGame == null) {
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
                    SERVER_FACADE.joinGame(authToken, tokens[2].toUpperCase(), Integer.parseInt(tokens[1]));
                    playerColor = tokens[2].toUpperCase();
                    System.out.printf("User %s has joined %d\n", username, Integer.parseInt(tokens[1]));
                } catch (Exception e) {
                    throw new FailException("Could not join game. Game may not exist.\n");
                }
                inGame = "yes";
                drawBoard(currentGame, playerColor, null);
                break;
            case "observe":
                inGame = "yes";
                playerColor = "WHITE";
                drawBoard(currentGame, playerColor, null);
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

    public static void gameUiCommands(String line){
        var tokens = line.split(" ");
        switch(tokens[0].toLowerCase()){
            case "help":
                //System.out.print("logout - to logout current user\n");
                System.out.print("highlight <piece> - highlights legal moves for piece\n");
                System.out.print("resign \n");
                System.out.print("move piece - move piece\n");
                System.out.print("draw - redraw chessboard\n");
                System.out.print("leave - to leave current game\n");
                break;
            case "exit":
            case "quit":
                throw new QuitException("quitting");
            case "draw":
                drawBoard(currentGame, playerColor, null);
                break;
            case "leave":
                inGame = null;
                playerColor = null;
                break;
            case "highlight":
                String piece = tokens[1];
                drawBoard(currentGame, playerColor, piece);
                break;
            default:
                System.out.print("You typed something wrong\n");
        }
    }

    private static Set<ChessPosition> getValidMoves(ChessPosition pose){
        Collection<ChessMove> moves = currentGame.validMoves(pose);
        Set<ChessPosition> positions = new HashSet<>();
        //String name = currentGame.getBoard().getPiece(new ChessPosition(firstNumber,secondNumber)).getPieceType().name();
        //System.out.print(name);
        for (ChessMove move : moves) {
            positions.add(move.getEndPosition());
        }

        return positions;
    }

    public static ChessPosition getPose(String piece){
        String firstLetter = piece.substring(0, 1);
        String secondLetter = piece.substring(1, 2);
        Map<String, Integer> letterToNumber = new HashMap<>();

        letterToNumber.put("a", 1);
        letterToNumber.put("b", 2);
        letterToNumber.put("c", 3);
        letterToNumber.put("d", 4);
        letterToNumber.put("e", 5);
        letterToNumber.put("f", 6);
        letterToNumber.put("g", 7);
        letterToNumber.put("h", 8);

        int secondNumber = letterToNumber.get(firstLetter);
        int firstNumber = Integer.parseInt(secondLetter);
        return new ChessPosition(firstNumber, secondNumber);
    }

    public static void drawBoard(ChessGame game, String color, String highlightPiece){
        Set<ChessPosition> positions = new HashSet<>();
        ChessPosition pose = null;
        if (highlightPiece != null){
            pose = getPose(highlightPiece);
            positions = getValidMoves(pose);
            //System.out.printf("pose is row %d col %d\n", pose.getRow(), pose.getColumn());
            //System.out.printf("piece is %s\n", currentGame.getBoard().getPiece(pose).getTeamColor().name());
        }

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

                if (!positions.isEmpty()){
                    if (positions.contains(new ChessPosition(8-i,j+1))) {
                        backgroundColor = (i + j) % 2 == 0 ? EscapeSequences.SET_BG_COLOR_GREEN_TINT_WHITE : EscapeSequences.SET_BG_COLOR_GREEN_TINT_BLACK;
                    } else {
                        backgroundColor = (i + j) % 2 == 0 ? EscapeSequences.SET_BG_COLOR_WHITE : EscapeSequences.SET_BG_COLOR_BLACK;
                    }
                } else {
                    backgroundColor = (i + j) % 2 == 0 ? EscapeSequences.SET_BG_COLOR_WHITE : EscapeSequences.SET_BG_COLOR_BLACK;
                }
                if (pose != null){
                    if (pose.equals(new ChessPosition(8-i,j+1))){
                        backgroundColor = (i + j) % 2 == 0 ? EscapeSequences.SET_BG_COLOR_YELLOW : EscapeSequences.SET_BG_COLOR_YELLOW;
                    }
                }

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