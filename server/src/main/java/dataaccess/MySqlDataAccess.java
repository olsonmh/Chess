package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.GameDataForListing;
import model.UserData;

import java.util.ArrayList;
import java.util.Collection;
import java.sql.*;
import java.util.HashSet;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

import org.mindrot.jbcrypt.BCrypt;


public class MySqlDataAccess implements GameDAO, UserDAO, AuthDAO {

    public MySqlDataAccess(){
        configureDatabase();
    }

    @Override
    public UserData getUser(String username){
        String statement = "SELECT name, passHash, email FROM user WHERE name = '%s';".formatted(username);
        try {
            ResultSet results = executeQuery(statement);
            return new UserData(results.getString(0), results.getString(1), results.getString(2));
        } catch (SQLException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public void clearUserData(){
        executeStatement("DELETE FROM user;");
    }

    @Override
    public void createUser(UserData user){
        String hashedPassword = BCrypt.hashpw(user.password(), BCrypt.gensalt());
        String statement = "INSERT INTO user (name, passHash, email) VALUES ('%s','%s','%s');"
                            .formatted(user.username(), hashedPassword, user.email());
        executeStatement(statement);
    }

    @Override
    public void createGame(GameData gameData){
        String json = new Gson().toJson(gameData.game());
        String statement = "INSERT INTO game (id, whiteUser, blackUser, name, state) VALUES (%d,'%s','%s','%s','%s');"
                .formatted(gameData.gameID(), gameData.whiteUsername(),gameData.blackUsername(),gameData.gameName(), json);
        executeStatement(statement);
    }

    @Override
    public GameData getGame(int gameID){
        String statement = "SELECT id, whiteUser, blackUser, name, state FROM game WHERE id = %d;".formatted(gameID);
        try {
            ResultSet results = executeQuery(statement);
            return new GameData(results.getInt(0),
                                results.getString(1),
                                results.getString(2),
                                results.getString(3),
                                new Gson().fromJson(results.getString(4), ChessGame.class));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Collection<GameDataForListing> listGames(){
        Collection<GameDataForListing> listOfGames = new HashSet<>();
        String statement = "SELECT id, whiteUser, blackUser, name FROM game;";
        try{
            ResultSet results = executeQuery(statement);
            while(results.next()){
                listOfGames.add(new GameDataForListing(results.getInt(0),
                                                       results.getString(1),
                                                       results.getString(2),
                                                       results.getString(3)));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return listOfGames;
    }

    @Override
    public void updateGame(GameData gameData){
        String jsonState = new Gson().toJson(gameData.game());
        String statement = "UPDATE game SET state = '%s' WHERE id = %d;".formatted(jsonState, gameData.gameID());
        executeStatement(statement);
    }

    @Override
    public void clearGames(){
        executeStatement("DELETE FROM game;");
    }

    @Override
    public void createAuth(AuthData authdata){
        String statement = "INSERT INTO auth (token, user) VALUES ('%s','%s');"
                            .formatted(authdata.authToken(), authdata.username());
        executeStatement(statement);
    }

    @Override
    public AuthData getAuth(String authToken) {
        String statement = "SELECT token, user FROM auth WHERE token = '%s';".formatted(authToken);
        ResultSet results = executeQuery(statement);
        try {
            return new AuthData(results.getString(0), results.getString(1));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAuth(String authToken){
        executeStatement("DELETE FROM auth WHERE token = '%s';".formatted(authToken));
    }

    @Override
    public void clearAuthTokens(){
        executeStatement("DELETE FROM auth;");

    }


    private ResultSet executeQuery(String statement) {
        try {
            var connection = DatabaseManager.getConnection();
            var prepareStatement = connection.prepareStatement(statement);
            return prepareStatement.executeQuery();
        } catch (DataAccessException | SQLException e){
            throw new RuntimeException();
        }
    }

    private void executeStatement(String statement) {
        try {
            var connection = DatabaseManager.getConnection();
            var prepareStatement = connection.prepareStatement(statement);
            prepareStatement.execute();
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private int executeUpdate(String statement, Object... params) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    //else if (param instanceof PetType p) ps.setString(i + 1, p.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException | DataAccessException e) {
            throw new RuntimeException();//ResponseException(500, String.format("unable to update database: %s, %s", statement, e.getMessage()))
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
              id int NOT NULL  PRIMARY KEY,
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


    private void configureDatabase(){
        try{
            DatabaseManager.createDatabase();
            var connection = DatabaseManager.getConnection();
            for(var statement : createStatements){
                var preparedStatement = connection.prepareStatement(statement);
                preparedStatement.executeUpdate();
            }
        } catch (DataAccessException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
    /**
    private void configureDatabase(){
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
    }**/
}