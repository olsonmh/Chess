package dataaccess;
import model.GameData;

import java.util.Collection;

public interface GameDAO {

    public int createGame(GameData gameData);
    public GameData getGame(int gameID);
    public Collection<GameData> listGames();
    public void updateGame(GameData gameData);
    public void clearGames();
}
