import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import model.*;
import com.google.gson.Gson;

public class ServerFacade {
    private final Gson serializer;

    public ServerFacade(){
        this.serializer = new Gson();
    }

    public <Ret,Obj> Ret poop(Obj obj, String endpoint, String method){
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

                return serializer.fromJson(response.toString(),Ret.class);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public AuthData register(String username, String password, String email){
        try{
            URL url = new URI("http://localhost:8080/user").toURL();
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);

            UserData user = new UserData(username,password, email);
            String json = serializer.toJson(user);

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

                return serializer.fromJson(response.toString(),AuthData.class);

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public void login(){

    }

    public void logout(){}

    public void createGame(){}

    public void joinGame(){}

    public void listGames(){}

    public void clearAll(){}

}
