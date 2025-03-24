package model.objects;

public record JoinGameRequest(String authToken, String playerColor, int gameID) {
}
