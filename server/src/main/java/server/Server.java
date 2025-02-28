package server;

import service.exceptions.*;
import spark.*;
import Handler.*;

import java.util.ResourceBundle;

public class Server {

    private final UserHandler userHandler = new UserHandler();
    private final GameHandler gameHandler = new GameHandler();
    private final ClearHandler clearHandler = new ClearHandler();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::registerUser);
        Spark.post("/session", this::loginUser);
        Spark.delete("/session", this::logoutUser);
        Spark.get("/game", this::listAllGames);
        Spark.post("/game", this::createNewGame);
        Spark.delete("/db", this::clear);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object registerUser(Request req, Response res) {
        try{
            String json = userHandler.registerUser(req.body());
            res.type("application/json");
            return json;
        } catch(UserExistException e){
            res.type("application/json");
            res.status(403);
            String error = "{ \"message\": \"Error: already taken\" }";
            res.body(error);
            return error;
        }

    }

    private Object loginUser(Request req, Response res) {
        try{
            String json = userHandler.loginUser(req.body());
            res.type("application/json");
            return json;
        } catch(WrongPasswordException e){
            res.type("application/json");
            res.status(401);
            String error = "{ \"message\": \"Error: unauthorized\" }";
            res.body(error);
            return error;
        } catch(UserNotFoundException e){
            res.type("application/json");
            res.status(404);
            String error = "{ \"message\": \"Error: user not found\" }";
            res.body(error);
            return error;
        }
    }

    private Object logoutUser(Request req, Response res){
        try{
            userHandler.logoutUser(req.body());
            res.type("application/json");
            return "{}";
        } catch(AuthTokenNotFoundException e){
            res.type("application/json");
            res.status(404);
            String error = "{ \"message\": \"Error: user not found\" }";
            res.body(error);
            return error;
        }
    }

    private Object listAllGames(Request req, Response res){
        try{
            String json = gameHandler.listAllGames(req.body());
            res.type("application/json");
            return json;
        } catch(AuthTokenNotFoundException e){
            res.type("application/json");
            res.status(401);
            String error = "{ \"message\": \"Error: unauthorized\" }";
            res.body(error);
            return error;
        }
    }

    private Object createNewGame(Request req, Response res){
        try{
            String json = gameHandler.createNewGame(req.body());
            res.type("application/json");
            return json;
        } catch(AuthTokenNotFoundException e){
            res.type("application/json");
            res.status(401);
            String error = "{ \"message\": \"Error: unauthorized\" }";
            res.body(error);
            return error;
        }
    }

    private Object joinNewGame(Request req, Response res){
        try{
            gameHandler.joinNewGame(req.body());
            res.type("application/json");
            return "{}";
        } catch(AuthTokenNotFoundException e){
            res.type("application/json");
            res.status(401);
            String error = "{ \"message\": \"Error: unauthorized\" }";
            res.body(error);
            return error;
        } catch(GameNotFoundException e){
            res.type("application/json");
            res.status(404);
            String error = "{ \"message\": \"Error: game not found\" }";
            res.body(error);
            return error;
        }
    }

    private Object clear(Request req, Response res){
        try{
            clearHandler.clear();
            res.type("application/json");
            return "{}";
        } catch(Exception e){
            res.type("application/json");
            res.status(500);
            String error = "{ \"message\": \"Error: unknown error\" }";
            res.body(error);
            return error;
        }
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
