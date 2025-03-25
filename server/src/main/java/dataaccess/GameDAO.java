package dataaccess;
import model.GameData;
import model.GameDataForListing;

import java.util.Collection;

public interface GameDAO {

    int createGame(GameData gameData);

    GameData getGame(int gameID);

    Collection<GameDataForListing> listGames();

    void updateGame(GameData gameData);

    void clearGames();

    void testSetup();

    void testRemove();
}
