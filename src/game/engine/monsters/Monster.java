package game.engine.monsters;

import game.engine.Constants;
import game.engine.Role;

public abstract class Monster implements Comparable<Monster> {
	private String name;
	private String description;
	private Role role;
	private Role originalRole;
	private int energy;
	private int position;
	private boolean frozen;
	private boolean shielded;
	private int confusionTurns;
	
	public Monster(String name, String description, Role originalRole, int energy) {
		super();
		this.name = name;
		this.description = description;
		this.role = originalRole;
		this.originalRole = originalRole; 
		this.energy = energy;
		this.position = 0;
		this.frozen = false;
		this.shielded = false;
		this.confusionTurns = 0;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}
	
	public Role getRole() {
		return role;
	}
	
	public void setRole(Role role) {
		this.role = role;
	}

	public Role getOriginalRole() {
		return originalRole;
	}

	public int getEnergy() {
		return energy;
	}

	public void setEnergy(int energy) {
		this.energy = Math.max(Constants.MIN_ENERGY, energy);
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position % Constants.BOARD_SIZE;
	}
	
	public boolean isFrozen() {
		return frozen;
	}
	
	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}
	
	public boolean isShielded() {
		return shielded;
	}
	
	public void setShielded(boolean shielded) {
		this.shielded = shielded;
	}
	
	public int getConfusionTurns() {
		return confusionTurns;
	}
	
	public void setConfusionTurns(int confusionTurns) {
		this.confusionTurns = confusionTurns;
	}

	@Override
	public int compareTo(Monster other) {
		return this.position - other.position;
	}
	// implement for each monster
	public abstract void executePowerupEffect(Monster opponentMonster);
	
	public boolean isConfused(){
		return confusionTurns >0;
	}
	
	public void move(int distance){
		int newPosition = this.position + distance;
		this.position = ((newPosition % Constants.BOARD_SIZE) + Constants.BOARD_SIZE) % Constants.BOARD_SIZE;
	}
	//implement for each monster (passive features)
	protected int altEnergyHelper(int energyChange) {
	    return energyChange;
	}

	public final void alterEnergy(int energyChange) {
	    if (this.shielded && energyChange < 0) {
	        this.shielded = false;
	        return;
	    }
	    int modifiedChange = altEnergyHelper(energyChange);
	    int newEnergy = this.energy + modifiedChange;
	    setEnergy(newEnergy);
	}
	
	public void decrementConfusion() {
        if (confusionTurns > 0) {
            confusionTurns--;
            if (confusionTurns == 0) {
                this.role = this.originalRole;
            }
        }
    }
	
}