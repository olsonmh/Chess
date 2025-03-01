package service.requestResult;
import model.GameData;
import model.GameDataForListing;

import java.util.Collection;

public record ListGamesResult(Collection<GameDataForListing> games) {
}
