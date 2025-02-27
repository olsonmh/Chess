package server;

import service.exceptions.UserExistException;
import spark.*;
import Handler.*;

public class Server {

    private UserHandler userHander = new UserHandler();

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", this::registerUser);

        //This line initializes the server and can be removed once you have a functioning endpoint 
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private Object registerUser(Request req, Response res) {
        try{
            String json = userHander.registerUser(req.body());
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

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
