package game.engine.cards;

import game.engine.monsters.*;

public class SwapperCard extends Card {

	public SwapperCard(String name, String description, int rarity) {
		super(name, description, rarity, true);
	}
	
	@Override
    public void performAction(Monster player, Monster opponent) {
        if (player.getPosition() < opponent.getPosition()) {
            int playerPos = player.getPosition();
            int opponentPos = opponent.getPosition();
            
            player.setPosition(opponentPos);
            opponent.setPosition(playerPos);
        }
    }
}
