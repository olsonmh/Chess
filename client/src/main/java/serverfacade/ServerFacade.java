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

    public <Ret,Obj> Ret doRequest(Obj obj, String endpoint, String method, Class<Ret> classType){
        try{
            URL url = new URI("http://localhost:8080/" + endpoint).toURL();
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod(method);
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);


            String json = serializer.toJson(obj);

            try(OutputStream outputStream = con.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            } catch (Exception e){
                throw new RuntimeException();
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

        try{
            URL url = new URI("http://localhost:8080/user").toURL();
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String json = serializer.toJson(registerRequest);

            try(OutputStream outputStream = con.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            } catch (Exception e){
                throw new RuntimeException();
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

                return serializer.fromJson(response.toString(),RegisterResult.class);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public LoginResult login(String username, String password){
        LoginRequest loginRequest = new LoginRequest(username,password);

        try{
            URL url = new URI("http://localhost:8080/session").toURL();
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            String json = serializer.toJson(loginRequest);

            try(OutputStream outputStream = con.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            } catch (Exception e){
                throw new RuntimeException();
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

                return serializer.fromJson(response.toString(),LoginResult.class);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } catch (Exception e){
            throw new RuntimeException(e);
        }

    }

    public String logout(String authToken){
        try{
            URL url = new URI("http://localhost:8080/session").toURL();
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("DELETE");
            con.setRequestProperty("Accept", "application/json");
            con.addRequestProperty("Authorization", authToken);


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

                return response.toString();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public CreateGameResult createGame(String authToken, String gameName){
        CreateGameRequest createGameRequest = new CreateGameRequest(authToken, gameName);

        try{
            URL url = new URI("http://localhost:8080/game").toURL();
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            con.addRequestProperty("Authorization", authToken);

            String json = serializer.toJson(createGameRequest);

            try(OutputStream outputStream = con.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            } catch (Exception e){
                throw new RuntimeException();
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

                return serializer.fromJson(response.toString(),CreateGameResult.class);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public String joinGame(String authToken, String playerColor, int gameID){
        JoinGameRequest joinGameRequest = new JoinGameRequest(authToken, playerColor, gameID);

        try{
            URL url = new URI("http://localhost:8080/game").toURL();
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("PUT");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            con.addRequestProperty("Authorization", authToken);

            String json = serializer.toJson(joinGameRequest);

            try(OutputStream outputStream = con.getOutputStream()) {
                byte[] input = json.getBytes(StandardCharsets.UTF_8);
                outputStream.write(input, 0, input.length);
            } catch (Exception e){
                throw new RuntimeException();
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

                return response.toString();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public ListGamesResult listGames(String authToken){

        try{
            URL url = new URI("http://localhost:8080/game").toURL();
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            //con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.addRequestProperty("Authorization", authToken);

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

                return serializer.fromJson(response.toString(),ListGamesResult.class);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static String clearAll(){
        try{
            URL url = new URI("http://localhost:8080/db").toURL();
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("DELETE");
            con.setRequestProperty("Accept", "application/json");
            //con.addRequestProperty("Authorization", authToken);


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

                return response.toString();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
