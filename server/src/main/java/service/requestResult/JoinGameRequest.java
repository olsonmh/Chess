package service.requestResult;

public record JoinGameRequest(String authToken, String playerColor, int gameID) {
}
