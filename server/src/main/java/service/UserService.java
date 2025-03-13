package service;

import org.mindrot.jbcrypt.BCrypt;
import service.exceptions.*;
import service.objects.*;
import model.*;

import java.util.Objects;

public class UserService extends Service{

    public RegisterResult register(RegisterRequest registerRequest) throws BadRegisterRequestException,
                                                                           UserExistException {
        if(registerRequest.username() == null || registerRequest.password() == null || registerRequest.email() == null){
            throw new BadRegisterRequestException("Invalid username, password, or email were given.");
        }

        UserData user = new UserData(registerRequest.username(),registerRequest.password(),registerRequest.email());

        if(userData.getUser(user.username()) != null){
            throw new UserExistException("There is already a user with that username.");
        }

        userData.createUser(user);
        AuthData auth = new AuthData(generateToken(),user.username());
        authData.createAuth(auth);
        return new RegisterResult(auth.username(), auth.authToken());
    }

    public LoginResult login(LoginRequest loginRequest) throws UserNotFoundException, WrongPasswordException {
        UserData user = userData.getUser(loginRequest.username());

        if(user == null){
            throw new UserNotFoundException("User not found.");
        }

        if(!BCrypt.checkpw(loginRequest.password(), user.password())){
            throw new WrongPasswordException("Incorrect Password.");
        }

        AuthData auth = new AuthData(generateToken(), loginRequest.username());
        authData.createAuth(auth);
        return new LoginResult(auth.username(), auth.authToken());
    }

    public void logout(LogoutRequest logoutRequest) throws AuthTokenNotFoundException{
        if(authData.getAuth(logoutRequest.authToken()) == null){
            throw new AuthTokenNotFoundException("Logout unsuccessful. AuthToken not found.");
        }

        authData.deleteAuth(logoutRequest.authToken());
    }
}
