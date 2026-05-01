package game.engine.monsters;

import game.engine.Constants;
import game.engine.Role;

public class MultiTasker extends Monster {
	private int normalSpeedTurns;
	
	public MultiTasker(String name, String description, Role role, int energy) {
		super(name, description, role, energy);
		this.normalSpeedTurns = 0;
	}

	public int getNormalSpeedTurns() {
		return normalSpeedTurns;
	}

	public void setNormalSpeedTurns(int normalSpeedTurns) {
		this.normalSpeedTurns = normalSpeedTurns;
	}
	//Passive features
	@Override
    public void move(int distance) {
		int actualDistance;
	    if (normalSpeedTurns > 0) {
	            actualDistance = distance;
	            normalSpeedTurns--;
	        } else {
	            actualDistance = distance / 2;
	        }
	        super.move(actualDistance);
	    }
	@Override
	protected int altEnergyHelper(int energyChange) {
		 return energyChange + Constants.MULTITASKER_BONUS;}

	//Focus Mode
	@Override
	public void executePowerupEffect(Monster opponentMonster) {
		this.normalSpeedTurns = 2;
	}
}