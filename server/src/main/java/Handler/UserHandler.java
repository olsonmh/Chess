package Handler;
import com.google.gson.Gson;
import chess.*;
import service.exceptions.UserExistException;
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


}
