package dataaccess;
import model.GameData;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MemoryGameDAO implements GameDAO{
    private final Map<Integer, GameData> memoryGameData;

    public MemoryGameDAO(){
        this.memoryGameData = new HashMap<>();
    }

    @Override
    public int createGame(GameData gameData) {
        this.memoryGameData.put(gameData.gameID(), gameData);
        return gameData.gameID();
    }

    @Override
    public GameData getGame(int gameID) {
        return this.memoryGameData.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() {
        return this.memoryGameData.values();
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
