package game.engine.cells;


import game.engine.Role;
import game.engine.interfaces.CanisterModifier;
import game.engine.monsters.Monster;
import game.engine.Board;

public class DoorCell extends Cell implements CanisterModifier {
	private Role role;
	private int energy;
	private boolean activated;

	public DoorCell(String name, Role role, int energy) {
		super(name);
		this.role = role;
		this.energy = energy;
		this.activated = false;
	}

	public Role getRole() {
		return role;
	}

	public int getEnergy() {
		return energy;
	}

	public boolean isActivated() {
		return activated;
	}

	public void setActivated(boolean isActivated) {
		this.activated = isActivated;
	}

	@Override
	public void onLand(Monster landingMonster, Monster opponentMonster) {
		super.onLand(landingMonster, opponentMonster);
		if (!activated) {
			modifyCanisterEnergy(landingMonster, energy);
		}
	}

	@Override
	public void modifyCanisterEnergy(Monster monster, int canisterValue) {
		int effectiveValue = (monster.getRole() == this.role) ? canisterValue : -canisterValue;
		boolean shieldBlocks = monster.isShielded() && effectiveValue < 0;
		monster.alterEnergy(effectiveValue);
		if (!shieldBlocks && effectiveValue != 0) {
			for (Monster stationed : Board.getStationedMonsters()) {
				if (stationed.getRole() == monster.getRole()) {
					stationed.alterEnergy(effectiveValue);
				}
			}
			this.activated = true;
		}
	}
}