package service;
import service.exceptions.*;
import service.requestResult.*;
import model.*;

public class UserService extends Service{

    public RegisterResult register(RegisterRequest registerRequest) {
        UserData user = new UserData(registerRequest.username(),registerRequest.password(),registerRequest.email());

        if(userData.getUser(user.username()) == null){
            userData.createUser(user);
            AuthData auth = new AuthData(generateToken(),user.username());
            authData.createAuth(auth);

            return new RegisterResult(auth.username(), auth.authToken());
        }
        throw new UserExistException("There is already a user with that username.");
    }

    public LoginResult login(LoginRequest loginRequest) {
        UserData user = userData.getUser(loginRequest.username());
        if(user != null){
            if(loginRequest.password().equals(user.password())){
                AuthData auth = new AuthData(generateToken(), loginRequest.username());
                authData.createAuth(auth);

                return new LoginResult(auth.username(), auth.authToken());
            }
            throw new WrongPasswordException("Incorrect Password.");
        }

        throw new UserNotFoundException("User not found.");
    }

    public void logout(LogoutRequest logoutRequest) {
        if(authData.getAuth(logoutRequest.authToken()) == null){
            throw new AuthTokenNotFoundException("Logout unsuccessful. AuthToken not found.");
        }
        authData.deleteAuth(logoutRequest.authToken());
    }
}
