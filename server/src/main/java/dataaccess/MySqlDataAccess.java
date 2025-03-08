package dataaccess;

import com.google.gson.Gson;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;


public class MySqlDataAccess implements GameDAO, UserDAO, AuthDAO {

    public MySqlDataAccess(){
        configureDatabase();
    }



    public void createUser(UserData user){
        var statement = "INSERT INTO pet (name, type, json) VALUES (?, ?, ?)";
        var json = new Gson().toJson(pet);
        var id = executeUpdate(statement, pet.name(), pet.type(), json);
        return new Pet(id, pet.name(), pet.type());
    }

    private int executeUpdate(String statement, Object... params) throws ResponseException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof PetType p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException e) {
            throw new ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()));
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS  user (
              name varchar(32) NOT NULL PRIMARY KEY,
              passHash varchar(256) NOT NULL,
              email varchar(256) DEFAULT NULL
            );
            CREATE TABLE IF NOT EXISTS game (
              id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
              whiteUser varchar(32) DEFAULT NULL,
              blackUser varchar(32) DEFAULT NULL,
              name varchar(256) NOT NULL,
              state TEXT DEFAULT NULL,
              FOREIGN KEY (whiteUser) REFERENCES user(name),
              FOREIGN KEY (blackUser) REFERENCES user(name)
            );
            CREATE TABLE IF NOT EXISTS auth (
              token varchar(64) NOT NULL PRIMARY KEY,
              user varchar(32) NOT NULL,
              FOREIGN KEY (user) REFERENCES user(name)
            );
            """
    };


    private void configureDatabase() throws RuntimeException{
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new ResponseException(500, String.format("Unable to configure database: %s", ex.getMessage()));
        }
    }
}