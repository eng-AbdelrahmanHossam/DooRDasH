package game.engine.interfaces;

import game.engine.monsters.Monster;

public interface CanisterModifier {
	// implement for DoorCell, ContaminationSock and EnergyStealCard
	public void modifyCanisterEnergy(Monster monster, int canisterValue);

}
