package service.objects;

public record JoinGameRequest(String authToken, String playerColor, int gameID) {
}
