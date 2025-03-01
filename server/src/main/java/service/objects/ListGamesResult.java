package service.objects;
import model.GameDataForListing;

import java.util.Collection;

public record ListGamesResult(Collection<GameDataForListing> games) {
}
