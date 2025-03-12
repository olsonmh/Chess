package dataaccess;

import model.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import dataaccess.MySqlDataAccess;
import dataaccess.DatabaseManager;
import service.UserService;
import service.exceptions.UserExistException;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

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
        } catch (SQLException e) {
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

}
