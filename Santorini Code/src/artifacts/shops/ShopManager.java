package artifacts.shops;

import GameMode.gameutils.TurnState;
import Player.Player;
import artifacts.Artifact;

public class ShopManager {
    public ShopManager() {}

    public boolean processPayment(Artifact artifact, Player player) {
        if (player.getTokens() >= artifact.getCost()) {
            player.decreaseTokens(artifact.getCost());
            player.addArtifact(artifact);
            return true;
        }
        return false;
    }

    public void closeShop(TurnState turnState){
        turnState.setBuyPhaseCompleted(true);
    }
}
