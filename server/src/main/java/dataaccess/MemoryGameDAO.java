package dataaccess;
import model.GameData;
import model.GameDataForListing;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.LinkedList;



public class MemoryGameDAO implements GameDAO{
    private final Map<Integer, GameData> memoryGameData;

    public MemoryGameDAO(){
        this.memoryGameData = new HashMap<>();
    }

    @Override
    public void createGame(GameData gameData) {
        this.memoryGameData.put(gameData.gameID(), gameData);
    }

    @Override
    public GameData getGame(int gameID) {
        return this.memoryGameData.get(gameID);
    }

    @Override
    public Collection<GameDataForListing> listGames() {
        return this.memoryGameData.values().stream().map(gameData -> new GameDataForListing(
                gameData.gameID(),
                gameData.whiteUsername(),
                gameData.blackUsername(),
                gameData.gameName()))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    public void updateGame(GameData gameData) {
        this.memoryGameData.remove(gameData.gameID());
        this.memoryGameData.put(gameData.gameID(), gameData);
    }

    @Override
    public void clearGames() {
        this.memoryGameData.clear();
    }
}
