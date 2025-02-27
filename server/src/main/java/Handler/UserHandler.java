package Handler;
import com.google.gson.Gson;
import chess.*;
import service.exceptions.AuthTokenNotFoundException;
import service.exceptions.UserExistException;
import service.exceptions.UserNotFoundException;
import service.exceptions.WrongPasswordException;
import service.requestResult.*;
import service.Service;
import service.UserService;


public class UserHandler {
    //Gson serializer = new Gson();
    //ChessGame game = new ChessGame();
    //String json = serializer.toJson(game);
    //ChessGame unSerialGame = serializer.fromJson(json, ChessGame.class);

    private final Gson serializer;
    private final UserService service = new UserService();

    public UserHandler(){
        this.serializer = new Gson();
    }

    public String registerUser(String json) throws UserExistException {
        RegisterRequest registerRequest = serializer.fromJson(json,RegisterRequest.class);

        RegisterResult registerResult = service.register(registerRequest);
        return serializer.toJson(registerResult);
    }

    public String loginUser(String json) throws WrongPasswordException, UserNotFoundException {
        LoginRequest loginRequest = serializer.fromJson(json, LoginRequest.class);
        LoginResult loginResult = service.login(loginRequest);
        return serializer.toJson(loginResult);
    }

    public void logoutUser(String json) throws AuthTokenNotFoundException {
        LogoutRequest logoutRequest = serializer.fromJson(json, LogoutRequest.class);
        service.logout(logoutRequest);
    }

}
