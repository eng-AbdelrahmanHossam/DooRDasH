package game.engine;

import java.io.IOException;
import java.util.*;
import game.engine.exceptions.*;
import game.engine.dataloader.DataLoader;
import game.engine.monsters.*;

public class Game {
	private Board board;
	private ArrayList<Monster> allMonsters; 
	private Monster player;
	private Monster opponent;
	private Monster current;
	
	public Game(Role playerRole) throws IOException {
		this.board = new Board(DataLoader.readCards());
		this.allMonsters = DataLoader.readMonsters();
		
		this.player = selectRandomMonsterByRole(playerRole);
		this.opponent = selectRandomMonsterByRole(playerRole == Role.SCARER ? Role.LAUGHER : Role.SCARER);
		this.current = player;
		
		ArrayList<Monster> stationedMonsters = new ArrayList<>(this.allMonsters);
		stationedMonsters.remove(this.player);
		stationedMonsters.remove(this.opponent);
		Board.setStationedMonsters(stationedMonsters);
		
		board.initializeBoard(DataLoader.readCells());
	}
	
	public Board getBoard() {
		return board;
	}
	
	public ArrayList<Monster> getAllMonsters() {
		return Board.getStationedMonsters(); 
	}
	
	public Monster getPlayer() {
		return player;
	}
	
	public Monster getOpponent() {
		return opponent;
	}
	
	public Monster getCurrent() {
		return current;
	}
	
	public void setCurrent(Monster current) {
		this.current = current;
	}
	
	private Monster selectRandomMonsterByRole(Role role) {
		List<Monster> candidates = new ArrayList<>();
		for (Monster m : allMonsters) {
			if (m.getRole() == role) candidates.add(m);
		}
		if (candidates.isEmpty()) return null;
		Collections.shuffle(candidates);
		return candidates.get(0);  // Do NOT remove from allMonsters
	}
	
	private Monster getCurrentOpponent(){
		if(current==player) return opponent;
		else return player;
	}
	
	private int rollDice(){
		Random rand = new Random();
		return rand.nextInt(6)+1;
	}
	
	public void usePowerup() throws OutOfEnergyException{
		if (current.getEnergy()<Constants.POWERUP_COST){
			throw new OutOfEnergyException();
		}
		current.alterEnergy(-Constants.POWERUP_COST);
		current.executePowerupEffect(opponent);
	}
	
	public void playTurn() throws InvalidMoveException{
		if(current.isFrozen()){
			current.setFrozen(false);
			}else{
				int diceROll = rollDice();
				board.moveMonster(current, diceROll, getCurrentOpponent());
			}
		switchTurn();
	}
	
	private void switchTurn(){
		if(current==player) current = opponent;
		else current=player;
	}
	
	private boolean checkWinCondition(Monster monster){
		return monster.getPosition()>= Constants.WINNING_POSITION &&
				monster.getEnergy()>= Constants.WINNING_ENERGY;
	}
	public Monster getWinner(){
		if(checkWinCondition(player)){
			return player;}
		if(checkWinCondition(opponent)){
			return opponent;
		}
		return null;
	}
}