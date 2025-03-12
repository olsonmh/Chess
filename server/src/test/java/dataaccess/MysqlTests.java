package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.GameDataForListing;
import model.UserData;
import service.Service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.List;
import java.util.Objects;



public class MysqlTests {
    private String old;

    @BeforeEach
    public void setup() {
        old = DatabaseManager.setDatabaseName("test");
        MySqlDataAccess.configureDatabase();
    }
    @AfterEach
    public void cleanup(){
        try {
            var connection = DatabaseManager.getConnection();
            var preparedStatement = connection.prepareStatement("drop database test");
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        DatabaseManager.setDatabaseName(old);
    }

    @Test
    @DisplayName("Configure Database Test")
    public void createDatabaseTest() {
        assert(true);

    }

    @Test
    @DisplayName("Positive Create User Test")
    public void positiveCreateUserTest() {
        MySqlDataAccess dao = new MySqlDataAccess();
        UserData user = new UserData("micah", "saooda3", "choom@hello.org");
        UserData user2 = new UserData("dad", "mypasswordsucks", "oldman@livessad.com");

        assertDoesNotThrow(() -> {dao.createUser(user);});
        assertDoesNotThrow(() -> {dao.createUser(user2);});

        String statement = "SELECT name, passHash, email FROM user WHERE name = '%s';".formatted("micah");
        try {
            ResultSet results = dao.executeQuery(statement);
            results.next();
            assert(results.getString("name").equals("micah"));
            assert(results.getString("email").equals("choom@hello.org"));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Negative Create User Test")
    public void negativeCreateUserTest() {
        MySqlDataAccess dao = new MySqlDataAccess();
        UserData user = new UserData("micah", "choom", "choom@hello.org");
        assertDoesNotThrow(() -> {dao.createUser(user);});
        assertThrows(Exception.class, () -> dao.createUser(user));
    }

    @Test
    @DisplayName("Positive Get User Test")
    public void positiveGetUserTest() {
        MySqlDataAccess dao = new MySqlDataAccess();
        UserData user = new UserData("micah", "saooda3", "choom@hello.org");

        assertDoesNotThrow(() -> {dao.createUser(user);});
        UserData userData = dao.getUser("micah");

        assert(userData.username().equals("micah"));
        assert(BCrypt.checkpw(user.password(), userData.password()));
    }

    @Test
    @DisplayName("Negative Get User Test")
    public void negativeGetUserTest() {
        MySqlDataAccess dao = new MySqlDataAccess();
        UserData user = new UserData("micah", "saooda3", "choom@hello.org");

        assertThrows(Exception.class, () -> dao.getUser("micah"));

        assertDoesNotThrow(() -> {dao.createUser(user);});
        UserData userData = dao.getUser("micah");
        assert !Objects.equals(userData.password(), user.password());
    }

    @Test
    @DisplayName("Clear User Test")
    public void clearUserTest() {
        MySqlDataAccess dao = new MySqlDataAccess();
        UserData user = new UserData("micah", "saooda3", "choom@hello.org");
        UserData user2 = new UserData("dad", "mypasswordsucks", "oldman@livessad.com");
        UserData user3 = new UserData("hello", "mypasswordsucks", "oldman@livessad.com");

        assertDoesNotThrow(() -> {dao.createUser(user);});
        assertDoesNotThrow(() -> {dao.createUser(user2);});
        assertDoesNotThrow(() -> {dao.createUser(user3);});

        assertDoesNotThrow(dao::clearUserData);

        assertThrows(Exception.class, () -> dao.getUser("micah"));
        assertThrows(Exception.class, () -> dao.getUser("dad"));
        assertThrows(Exception.class, () -> dao.getUser("hello"));
    }

    @Test
    @DisplayName("Positive Create Game Test")
    public void positiveCreateGameTest() {
        MySqlDataAccess dao = new MySqlDataAccess();
        GameData game = new GameData(123456, null, null,"myGame", new ChessGame());

        assertDoesNotThrow(() -> {dao.createGame(game);});

        String statement = "SELECT id, whiteUser, blackUser, name, state FROM game WHERE id = 123456;";
        try {
            ResultSet results = dao.executeQuery(statement);
            results.next();
            GameData gameData = new GameData(results.getInt("id"),
                                             results.getString("whiteUser"),
                                             results.getString("blackUser"),
                                             results.getString("name"),
                                             new Gson().fromJson(results.getString("state"), ChessGame.class));
            assert(gameData.gameID() == 123456);
            assert(gameData.gameName().equals("myGame"));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Negative Create Game Test")
    public void negativeCreateGameTest() {
        MySqlDataAccess dao = new MySqlDataAccess();
        GameData game = new GameData(123456, null, null,null, new ChessGame());

        assertThrows(Exception.class, () -> dao.createGame(game));
    }

    @Test
    @DisplayName("Positive Get Game Test")
    public void positiveGetGameTest() {
        MySqlDataAccess dao = new MySqlDataAccess();
        GameData game = new GameData(123456, null, null,"myGame", new ChessGame());

        assertDoesNotThrow(() -> {dao.createGame(game);});
        try{
            GameData gameData = dao.getGame(123456);
            assert(gameData.gameName().equals("myGame"));
            assert(gameData.gameID() == 123456);
            assert(gameData.whiteUsername() == null);
            assert(gameData.blackUsername() == null);
            assert(gameData.game().equals(new ChessGame()));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Negative Get Game Test")
    public void negativeGetGameTest() {
        MySqlDataAccess dao = new MySqlDataAccess();
        GameData game = new GameData(123456, null, null,"myGame", new ChessGame());

        assertDoesNotThrow(() -> {dao.createGame(game);});
        assertThrows(Exception.class, () -> dao.getGame(12346));
    }

    @Test
    @DisplayName("Positive Get Game List Test")
    public void positiveGetGameListTest() {
        MySqlDataAccess dao = new MySqlDataAccess();
        GameData game = new GameData(123456, null, null,"myGame", new ChessGame());
        GameData game2 = new GameData(121236, null, null,"CoolGame", new ChessGame());

        assertDoesNotThrow(() -> {dao.createGame(game);});
        assertDoesNotThrow(() -> {dao.createGame(game2);});

        try{
            Collection<GameDataForListing> listGames = dao.listGames();
            List<String> expectedGameNames = List.of("myGame", "CoolGame");
            List<String> actualGameNames = listGames.stream().map(GameDataForListing::gameName).toList();
            assertTrue(actualGameNames.containsAll(expectedGameNames));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Negative Get Game List Test")
    public void negativeGetGameListTest() {
        MySqlDataAccess dao = new MySqlDataAccess();
        GameData game = new GameData(123456, null, null,"ThisIsACoolGame", new ChessGame());
        GameData game2 = new GameData(121236, null, null,"NewGame", new ChessGame());
        GameData game3 = new GameData(123111, null, null,"NewGame", new ChessGame());

        assertDoesNotThrow(() -> {dao.createGame(game);});
        assertDoesNotThrow(() -> {dao.createGame(game2);});
        assertDoesNotThrow(() -> {dao.createGame(game3);});

        try{
            Collection<GameDataForListing> listGames = dao.listGames();
            List<String> expectedGameNames = List.of("ThisIsACoolGame", "NewGame", "otherGame");
            List<String> actualGameNames = listGames.stream().map(GameDataForListing::gameName).toList();
            assertFalse(actualGameNames.containsAll(expectedGameNames));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Positive Update Game Test")
    public void positiveUpdateGameTest() {
        MySqlDataAccess dao = new MySqlDataAccess();
        GameData game = new GameData(123456, null, null,"myGame", new ChessGame());
        GameData updatedGame = new GameData(123456, "Micah", null,"myGame", new ChessGame());
        UserData user = new UserData("Micah", "saooda3", "choom@hello.org");

        assertDoesNotThrow(() -> {dao.createGame(game);});
        assertDoesNotThrow(() -> {dao.createUser(user);});

        try{
            dao.updateGame(updatedGame);
            GameData gameData = dao.getGame(123456);
            assert(gameData.gameName().equals("myGame"));
            assert(gameData.whiteUsername().equals("Micah"));
            assert(gameData.blackUsername() == null);
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @DisplayName("Negative Update Game Test")
    public void negativeUpdateGameTest() {
        MySqlDataAccess dao = new MySqlDataAccess();
        GameData game = new GameData(123456, null, null,"myGame", new ChessGame());
        GameData updatedGame = new GameData(123456, "Micah", null,"myGame", new ChessGame());

        assertDoesNotThrow(() -> {dao.createGame(game);});
        assertThrows(Exception.class, () -> dao.updateGame(updatedGame));
    }

    @Test
    @DisplayName("Clear Games Test")
    public void clearGamesTest() {
        MySqlDataAccess dao = new MySqlDataAccess();
        GameData game = new GameData(123456, null,null,"myGame", new ChessGame());
        GameData game2 = new GameData(123457, null,null,"myGame2", new ChessGame());
        GameData game3 = new GameData(123458, null,null,"myGame3", new ChessGame());

        assertDoesNotThrow(() -> {dao.createGame(game);});
        assertDoesNotThrow(() -> {dao.createGame(game2);});
        assertDoesNotThrow(() -> {dao.createGame(game3);});
        assertDoesNotThrow(dao::clearGames);

        assertThrows(Exception.class, () -> dao.getGame(123456));
        assertThrows(Exception.class, () -> dao.getGame(123457));
        assertThrows(Exception.class, () -> dao.getGame(123458));
    }

    @Test
    @DisplayName("Positive Create Auth Test")
    public void positiveCreateAuthTest() {
        MySqlDataAccess dao = new MySqlDataAccess();
        UserData user = new UserData("micah", "saooda3", "choom@hello.org");
        UserData user2 = new UserData("charles", "mypasswordsucks", "oldman@livessad.com");
        AuthData auth = new AuthData(Service.generateToken(), "micah");
        AuthData auth2 = new AuthData(Service.generateToken(), "charles");

        assertDoesNotThrow(() -> {dao.createUser(user);});
        assertDoesNotThrow(() -> {dao.createUser(user2);});
        assertDoesNotThrow(() -> {dao.createAuth(auth);});
        assertDoesNotThrow(() -> {dao.createAuth(auth2);});
    }

    @Test
    @DisplayName("Negative Create Auth Test")
    public void negativeCreateAuthTest() {
        MySqlDataAccess dao = new MySqlDataAccess();
        AuthData auth = new AuthData(Service.generateToken(), "micah");
        AuthData auth2 = new AuthData(Service.generateToken(), "charles");

        assertThrows(Exception.class, () -> dao.createAuth(auth));
        assertThrows(Exception.class, () -> dao.createAuth(auth2));
    }

    @Test
    @DisplayName("Positive Get Auth Test")
    public void positiveGetAuthTest() {
        MySqlDataAccess dao = new MySqlDataAccess();
        UserData user = new UserData("Choom", "password", "choom@hello.org");
        UserData user2 = new UserData("Greg", "helpme", "greg@email.com");
        AuthData auth = new AuthData(Service.generateToken(), "Choom");
        AuthData auth2 = new AuthData(Service.generateToken(), "Greg");

        dao.createUser(user);
        dao.createUser(user2);
        dao.createAuth(auth);
        dao.createAuth(auth2);

        AuthData authData = dao.getAuth(auth.authToken());
        AuthData authData2 = dao.getAuth(auth2.authToken());

        assert(authData.username().equals(auth.username()));
        assert(authData.authToken().equals(auth.authToken()));
        assert(authData2.username().equals(auth2.username()));
        assert(authData2.authToken().equals(auth2.authToken()));
    }

    @Test
    @DisplayName("Negative Get Auth Test")
    public void negativeGetAuthTest() {
        MySqlDataAccess dao = new MySqlDataAccess();
        AuthData auth = new AuthData(Service.generateToken(), "Micah");
        AuthData auth2 = new AuthData(Service.generateToken(), "Greg");

        assertThrows(Exception.class, () -> dao.getAuth(auth.authToken()));
        assertThrows(Exception.class, () -> dao.getAuth(auth2.authToken()));
    }

    @Test
    @DisplayName("Negative Delete Auth Test")
    public void negativeDeleteAuthTest() {
        MySqlDataAccess dao = new MySqlDataAccess();
        UserData user = new UserData("Micah", "password", "choom@hello.org");
        UserData user2 = new UserData("Greg", "helpme", "greg@email.com");
        AuthData auth = new AuthData(Service.generateToken(), "Micah");
        AuthData auth2 = new AuthData(Service.generateToken(), "Greg");

        dao.createUser(user);
        dao.createUser(user2);
        dao.createAuth(auth);
        dao.createAuth(auth2);

        dao.deleteAuth(auth.authToken());
        dao.deleteAuth(auth2.authToken());

        assertThrows(Exception.class, () -> dao.getAuth(auth.authToken()));
        assertThrows(Exception.class, () -> dao.getAuth(auth2.authToken()));
    }

    @Test
    @DisplayName("Positive Delete Auth Test")
    public void positiveDeleteAuthTest() {
        MySqlDataAccess dao = new MySqlDataAccess();
        AuthData auth = new AuthData(Service.generateToken(), "Micah");
        AuthData auth2 = new AuthData(Service.generateToken(), "Greg");

        assertDoesNotThrow(() -> {dao.deleteAuth(auth.authToken());});
        assertDoesNotThrow(() -> {dao.deleteAuth(auth2.authToken());});
    }

    @Test
    @DisplayName("Clear Auth Test")
    public void clearAuthTest() {
        MySqlDataAccess dao = new MySqlDataAccess();
        UserData user = new UserData("Master", "password", "choom@hello.org");
        UserData user2 = new UserData("Greg", "helpme", "greg@email.com");
        UserData user3 = new UserData("micah", "whatIsMyName", "micah@email.com");
        AuthData auth = new AuthData(Service.generateToken(), "Master");
        AuthData auth2 = new AuthData(Service.generateToken(), "Greg");
        AuthData auth3 = new AuthData(Service.generateToken(), "micah");

        dao.createUser(user);
        dao.createUser(user2);
        dao.createUser(user3);
        dao.createAuth(auth);
        dao.createAuth(auth2);
        dao.createAuth(auth3);

        assertDoesNotThrow(dao::clearAuthTokens);
        assertThrows(Exception.class, () -> dao.getAuth(auth.authToken()));
        assertThrows(Exception.class, () -> dao.getAuth(auth2.authToken()));
        assertThrows(Exception.class, () -> dao.getAuth(auth3.authToken()));
    }

    @Test
    @DisplayName("Positive Execute Query Test")
    public void positiveExecuteQueryTest() {
        MySqlDataAccess dao = new MySqlDataAccess();
        UserData user = new UserData("Master", "password", "choom@hello.org");
        dao.createUser(user);
        String statement = "SELECT name, passHash, email FROM user WHERE name = 'Master';";
        assertDoesNotThrow(() -> {dao.executeQuery(statement);});
    }

    @Test
    @DisplayName("Negative Execute Query Test")
    public void negativeExecuteQueryTest() {
        MySqlDataAccess dao = new MySqlDataAccess();
        UserData user = new UserData("Master", "password", "choom@hello.org");
        dao.createUser(user);
        String statement = "SELECT name, passHash,  user WHERE name = 'choom';";
        assertThrows(Exception.class, () -> dao.executeQuery(statement));
    }

    @Test
    @DisplayName("Positive Execute Statement Test")
    public void positiveExecuteStatementTest() {
        MySqlDataAccess dao = new MySqlDataAccess();
        UserData user = new UserData("Master", "password", "choom@hello.org");
        dao.createUser(user);
        String statement = "DELETE FROM user;";
        assertDoesNotThrow(() -> {dao.executeStatement(statement);});
    }

    @Test
    @DisplayName("Negative Execute Statement Test")
    public void negativeExecuteStatementTest() {
        MySqlDataAccess dao = new MySqlDataAccess();
        UserData user = new UserData("Master", "password", "choom@hello.org");
        dao.createUser(user);
        String statement = "DELETE FROM choom;";
        assertThrows(Exception.class, () -> dao.executeStatement(statement));
    }

}
