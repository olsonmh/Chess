package dataaccess;
import model.GameData;
import model.GameDataForListing;

import java.util.Collection;

public interface GameDAO {

    void createGame(GameData gameData);

    GameData getGame(int gameID);

    Collection<GameDataForListing> listGames();

    void updateGame(GameData gameData);

    void clearGames();
}
