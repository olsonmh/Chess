package server;

import service.exceptions.*;
import spark.*;
import handler.*;


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
        Spark.put("/game", this::joinNewGame);
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
        } catch(BadRegisterRequestException e){
            res.type("application/json");
            res.status(400);
            String error = "{ \"message\": \"Error: bad request\" }";
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
            res.status(401);
            String error = "{ \"message\": \"Error: user not found\" }";
            res.body(error);
            return error;
        }
    }

    private Object logoutUser(Request req, Response res){
        try{
            userHandler.logoutUser(req.headers("authorization"));
            return "{}";
        } catch(AuthTokenNotFoundException e){
            res.type("application/json");
            res.status(401);
            String error = "{ \"message\": \"Error: unauthorized\" }";
            res.body(error);
            return error;
        }
    }

    private Object listAllGames(Request req, Response res){
        try{
            String json = gameHandler.listAllGames(req.headers("authorization"));
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
            String json = gameHandler.createNewGame(req.headers("authorization"), req.body());
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
            gameHandler.joinNewGame(req.headers("authorization"), req.body());
            return "{}";
        } catch(AuthTokenNotFoundException e){
            res.type("application/json");
            res.status(401);
            String error = "{ \"message\": \"Error: unauthorized\" }";
            res.body(error);
            return error;
        } catch(GameNotFoundException e){
            res.type("application/json");
            res.status(400);
            String error = "{ \"message\": \"Error: bad request\" }";
            res.body(error);
            return error;
        } catch(PlayerAlreadyTakenException e) {
            res.type("application/json");
            res.status(403);
            String error = "{ \"message\": \"Error: player color already in use.\" }";
            res.body(error);
            return error;
        } catch(WrongColorException e) {
            res.type("application/json");
            res.status(400);
            String error = "{ \"message\": \"Error: team color does not exist\" }";
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
