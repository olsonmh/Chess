import chess.*;
import java.util.Scanner;

import model.GameDataForListing;
import model.objects.CreateGameResult;
import model.objects.ListGamesResult;
import model.objects.LoginResult;
import ui.EscapeSequences;

import model.objects.RegisterResult;
import serverfacade.ServerFacade;
import ui.exceptions.QuitException;

public class Main {
    private final static ServerFacade serverFacade = new ServerFacade();
    private static String username;
    private static String authToken;

    public static void main(String[] args) {
        System.out.print("Welcome to chess\n");
        System.out.print("Type help for more information\n");
        try {
            while (true) {
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
                LoginResult loginResult = serverFacade.login(tokens[1], tokens[2]);
                username = loginResult.username();
                authToken = loginResult.authToken();
                break;
            case "register":
                RegisterResult result = serverFacade.register(tokens[1], tokens[2], tokens[3]);
                username = result.username();
                authToken = result.authToken();
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
                serverFacade.logout(authToken);
                authToken = null;
                username = null;
                break;
            case "create":
                CreateGameResult gameResult = serverFacade.createGame(authToken, tokens[1]);
                System.out.printf("Game %s created with ID: %d\n", tokens[1], gameResult.gameID());
                break;
            case "list":
                ListGamesResult listGamesResult = serverFacade.listGames(authToken);
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
                serverFacade.joinGame(authToken,tokens[2],Integer.parseInt(tokens[1]));
                System.out.printf("User %s has joined %d\n", username, Integer.parseInt(tokens[1]));
                break;
            case "observe":
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

}