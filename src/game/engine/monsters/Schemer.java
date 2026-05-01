package game.engine.monsters;

import game.engine.Board;
import game.engine.Role;
import game.engine.Constants;
public class Schemer extends Monster {
	
	public Schemer(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
	}
	private int stealEnergyFrom(Monster target) {
        int stealAmount = Constants.SCHEMER_STEAL;
        if (target.getEnergy() < stealAmount) {
            stealAmount = target.getEnergy();
        }
        target.alterEnergy(-stealAmount);
        return stealAmount;
    }
	//Passive features
	@Override
	protected int altEnergyHelper(int energyChange) {
		return energyChange + Constants.SCHEMER_STEAL;
	}
    //Chain attack
    @Override
    public void executePowerupEffect(Monster opponentMonster) {
        int totalStolen = 0;
        totalStolen += stealEnergyFrom(opponentMonster);
        for(Monster stationed:Board.getStationedMonsters()){
        	totalStolen+=stealEnergyFrom(stationed);
        }
        this.alterEnergy(totalStolen);
    }
}
