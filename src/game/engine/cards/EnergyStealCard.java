package game.engine.cards;

import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.*;

public class EnergyStealCard extends Card implements CanisterModifier {
	private int energy;

	public EnergyStealCard(String name, String description, int rarity, int energy) {
		super(name, description, rarity, true);
		this.energy = energy;
	}
	
	public int getEnergy() {
		return energy;
	}
	
	@Override
	public void performAction(Monster player, Monster opponent) {
		int toSteal = Math.min(energy, opponent.getEnergy());
		boolean opponentShieldedBefore = opponent.isShielded();
		modifyCanisterEnergy(opponent, -toSteal);
 		int actuallyStolen;
		if (opponentShieldedBefore && toSteal > 0) {
			actuallyStolen = 0;
		} else {
			actuallyStolen = toSteal;
		}
 		modifyCanisterEnergy(player, actuallyStolen);
	}
 
	@Override
	public void modifyCanisterEnergy(Monster monster, int canisterValue) {
		monster.alterEnergy(canisterValue);
	}
}
