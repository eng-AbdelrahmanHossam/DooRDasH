package game.engine.monsters;

import game.engine.Role;

public class Dynamo extends Monster {
	
	public Dynamo(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
	}
	
	//Passive Feature
	@Override
	protected int altEnergyHelper(int energyChange) {
	    return energyChange * 2;
	}
	//Screech Freeze
	@Override
	public void executePowerupEffect(Monster opponentMonster) {
		opponentMonster.setFrozen(true);
	}
}
