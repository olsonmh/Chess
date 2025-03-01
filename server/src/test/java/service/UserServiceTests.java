package service;

import model.*;
import service.exceptions.*;
import service.requestResult.*;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Objects;

public class UserServiceTests {
    private UserService userService;

    @BeforeEach
    public void setup() {
        userService = new UserService();
    }

    @AfterEach
    public void remove(){
        Service.userData.clearUserData();
    }

    @Test
    @DisplayName("Positive Register Test")
    public void positiveRegisterTest() {
        RegisterRequest registerRequest = new RegisterRequest("Micah", "hello", "micah@email.com");
        RegisterResult registerResult = userService.register(registerRequest);
        assert Objects.equals(registerResult.username(), "Micah");
        assert registerResult.authToken() != null;

        UserData user = Service.userData.getUser("Micah");

        assert Objects.equals(user.username(), "Micah");
        assert Objects.equals(user.password(), "hello");
        assert Objects.equals(user.email(), "micah@email.com");
    }

    @Test
    @DisplayName("Negative Register Test")
    public void negativeRegisterTest() {
        RegisterRequest registerRequest = new RegisterRequest("Micah", "hello", "micah@email.com");
        userService.register(registerRequest);

        RegisterRequest registerRequest2 = new RegisterRequest("Micah", "funTimes", "ineedanemail@email.com");
        assertThrows(UserExistException.class, () -> {
            userService.register(registerRequest2);
        });
    }


    @Test
    @DisplayName("Positive Login Test")
    public void positiveLoginTest() {
        RegisterRequest registerRequest = new RegisterRequest("Micah", "hello", "micah@email.com");
        userService.register(registerRequest);

        LoginRequest loginRequest = new LoginRequest("Micah", "hello");
        LoginResult loginResult = userService.login(loginRequest);
        assert Objects.equals(loginResult.username(), "Micah");
        assert loginResult.authToken() != null;
    }

    @Test
    @DisplayName("Negative Login Test")
    public void negativeLoginTest() {
        LoginRequest loginRequest = new LoginRequest("Micah", "hello");

        assertThrows(UserNotFoundException.class, () -> {
            userService.login(loginRequest);
        });

        RegisterRequest registerRequest = new RegisterRequest("Geoff", "password", "Geoff@email.com");
        userService.register(registerRequest);
        LoginRequest loginRequest2 = new LoginRequest("Geoff", "passwords");

        assertThrows(WrongPasswordException.class, () -> {
            userService.login(loginRequest2);
        });
    }

    @Test
    @DisplayName("Positive Logout Test")
    public void positiveLogoutTest() {
        RegisterRequest registerRequest = new RegisterRequest("Micah", "hello", "micah@email.com");
        RegisterResult registerResult = userService.register(registerRequest);
        String authToken = registerResult.authToken();

        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        userService.logout(logoutRequest);

        assert Service.authData.getAuth(authToken) == null;
    }

    @Test
    @DisplayName("Negative Logout Test")
    public void negativeLogoutTest() {
        RegisterRequest registerRequest = new RegisterRequest("Micah", "hello", "micah@email.com");
        RegisterResult registerResult = userService.register(registerRequest);
        String authToken = registerResult.authToken();

        LogoutRequest logoutRequest = new LogoutRequest(authToken);
        userService.logout(logoutRequest);

        LogoutRequest logoutRequest2 = new LogoutRequest(authToken);
        assertThrows(AuthTokenNotFoundException.class, () -> {
            userService.logout(logoutRequest2);
        });
    }

}
