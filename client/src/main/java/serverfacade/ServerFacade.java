package serverfacade;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.Class;

import com.google.gson.Gson;
import model.objects.*;

public class ServerFacade {
    private final Gson serializer;

    public ServerFacade(){
        this.serializer = new Gson();
    }

    private <T,O> O doRequest(T req,
                                                 String endpoint,
                                                 String method,
                                                 Class<O> classType,
                                                 String authToken){
        try{
            URL url = new URI("http://localhost:8080/" + endpoint).toURL();
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod(method);

            con.setRequestProperty("Accept", "application/json");
            if (authToken != null){
                con.addRequestProperty("Authorization", authToken);
            }

            if (req != null) {
                con.setRequestProperty("Content-Type", "application/json");
                con.setDoOutput(true);

                String json = serializer.toJson(req);

                try (OutputStream outputStream = con.getOutputStream()) {
                    byte[] input = json.getBytes(StandardCharsets.UTF_8);
                    outputStream.write(input, 0, input.length);
                } catch (Exception e) {
                    throw new RuntimeException();
                }
            }
            int responseCode = con.getResponseCode();
            if(responseCode != 200){
                throw new RuntimeException(con.getResponseMessage());
            }

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                if (classType.equals(String.class)){
                    return classType.cast(response.toString());
                }
                return serializer.fromJson(response.toString(), classType);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public RegisterResult register(String username, String password, String email){
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        return doRequest(registerRequest, "/user", "POST", RegisterResult.class, null);
    }

    public LoginResult login(String username, String password){
        LoginRequest loginRequest = new LoginRequest(username,password);
        return doRequest(loginRequest, "/session", "POST", LoginResult.class, null);
    }

    public String logout(String authToken){
        return doRequest(null, "/session", "DELETE", String.class, authToken);
    }

    public CreateGameResult createGame(String authToken, String gameName){
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, gameName);
        return doRequest(createGameRequest, "/game", "POST", CreateGameResult.class, authToken);
    }

    public String joinGame(String authToken, String playerColor, int gameID){
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, playerColor, gameID);
        return doRequest(joinGameRequest, "/game", "PUT", String.class, authToken);
    }

    public ListGamesResult listGames(String authToken){
        return doRequest(null, "/game", "GET", ListGamesResult.class, authToken);
    }

    public String clearAll(){
        return doRequest(null, "/db", "DELETE", String.class, null);
    }
}
