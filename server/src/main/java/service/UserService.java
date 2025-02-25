package service;
import service.requestResult.*;

public class UserService extends Service{
    public RegisterResult register(RegisterRequest registerRequest) {
        throw new RuntimeException();
    }
    public LoginResult login(LoginRequest loginRequest) {
        throw new RuntimeException();
    }
    public void logout(LogoutRequest logoutRequest) {}
}
