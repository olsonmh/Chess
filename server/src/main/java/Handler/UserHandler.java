package Handler;

import service.exceptions.*;
import service.requestResult.*;
import service.UserService;

import com.google.gson.Gson;

public class UserHandler {

    private final Gson serializer;
    private final UserService service = new UserService();

    public UserHandler(){
        this.serializer = new Gson();
    }

    public String registerUser(String json) throws UserExistException, BadRegisterRequestException {
        RegisterRequest registerRequest = serializer.fromJson(json,RegisterRequest.class);

        RegisterResult registerResult = service.register(registerRequest);
        return serializer.toJson(registerResult);
    }

    public String loginUser(String json) throws WrongPasswordException, UserNotFoundException {
        LoginRequest loginRequest = serializer.fromJson(json, LoginRequest.class);
        LoginResult loginResult = service.login(loginRequest);
        return serializer.toJson(loginResult);
    }

    public void logoutUser(String authToken) throws AuthTokenNotFoundException {
        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        service.logout(logoutRequest);
    }

}
