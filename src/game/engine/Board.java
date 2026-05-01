package game.engine;

import java.util.*;

import game.engine.cards.Card;
import game.engine.cells.*;
import game.engine.monsters.Monster;
import game.engine.exceptions.*;

public class Board {
	private Cell[][] boardCells;
	private static ArrayList<Monster> stationedMonsters; 
	private static ArrayList<Card> rawCards;
	private static ArrayList<Card> originalCards; 
	public static ArrayList<Card> cards;

	
	public Board(ArrayList<Card> readCards) {
		this.boardCells = new Cell[Constants.BOARD_ROWS][Constants.BOARD_COLS];
		stationedMonsters = new ArrayList<Monster>();
		rawCards = readCards;
		originalCards = new ArrayList<Card>(readCards);
		cards = new ArrayList<Card>();
		
		setCardsByRarity();
		reloadCards();
	}
	
	public Cell[][] getBoardCells() {
		return boardCells;
	}
	
	public static ArrayList<Monster> getStationedMonsters() {
		return stationedMonsters;
	}
	
	public static void setStationedMonsters(ArrayList<Monster> stationedMonsters) {
		Board.stationedMonsters = stationedMonsters;
	}

	public static ArrayList<Card> getOriginalCards() {
		return rawCards;
	}
	
	public static ArrayList<Card> getCards() {
		return cards;
	}
	
	public static void setCards(ArrayList<Card> cards) {
		Board.cards = cards;
	}
	
	private int[] indexToRowCol(int index){
		int row = index / Constants.BOARD_COLS;
		int col;
		
		if(row % 2 == 0){
			col = index % Constants.BOARD_COLS;
		}
		else{
			col=(Constants.BOARD_COLS-1)-(index%Constants.BOARD_COLS);}
		return new int[]{row,col};
			}
	private Cell getCell(int index){
		int[] rowCol= indexToRowCol(index);
		return boardCells[rowCol[0]][rowCol[1]];
	}
	private void setCell(int index, Cell cell){
		int[] rowCol= indexToRowCol(index);
		boardCells[rowCol[0]][rowCol[1]] = cell;
	}
	
	public void initializeBoard(ArrayList<Cell> specialCells){
		ArrayList<Cell> doorCells = new ArrayList<Cell>();
		for (Cell c : specialCells) {
			if (c instanceof DoorCell) doorCells.add(c);
		}

		int doorIndex = 0;
		for(int i=0; i<Constants.BOARD_SIZE;i++){
			if(i%2==0){
				setCell(i, new Cell("Rest Cell"));
			}else{
				if (doorIndex < doorCells.size()) {
					setCell(i, doorCells.get(doorIndex++));
				} else {
					setCell(i, new DoorCell("Door", Role.SCARER, 0));
				}
			}
		}
		//card
		for(int index: Constants.CARD_CELL_INDICES){
			setCell(index, new CardCell("Card Cell"));
		}
		//conveyorbelt
		for(int index: Constants.CONVEYOR_CELL_INDICES){
			for(Cell cell:specialCells){
				if(cell instanceof ConveyorBelt){
					setCell(index, cell);
					break;
				}
			}
		}
		//contamination
		for(int index:Constants.SOCK_CELL_INDICES){
			for(Cell cell:specialCells){
				if(cell instanceof ContaminationSock){
					setCell(index, cell);
					break;
				}
			}
		}
		//monster
		int monsterIndex = 0;
		ArrayList<Monster> stationed = Board.getStationedMonsters();

		for(int index : Constants.MONSTER_CELL_INDICES){
		    if(monsterIndex < stationed.size()){
		        Monster monster = stationed.get(monsterIndex);
		        monster.setPosition(index);  // set monster's position to its cell index
		        MonsterCell monsterCell = new MonsterCell(monster.getName(), monster);
		        setCell(index, monsterCell);
		        monsterIndex++;
		    } else {
		        for(Cell cell : specialCells){
		            if(cell instanceof MonsterCell){
		                setCell(index, cell);
		                break;
		            }
		        }
		    }
		}
		}
	
	private void setCardsByRarity(){
		ArrayList<Card> expanded = new ArrayList<Card>();
		for(Card card:originalCards){
			for(int i=0;i<card.getRarity();i++){
				expanded.add(card);
			}
		}
		originalCards = expanded;
	}
	
	public static void reloadCards(){
		cards=new ArrayList<Card>(originalCards);
		Collections.shuffle(cards);
	}
	
	public static Card drawCard(){
		if(cards.isEmpty()){
			reloadCards();
		}
		return cards.remove(0);
	}
	
	public void moveMonster(Monster currentMonster, int roll, Monster opponentMonster) throws InvalidMoveException{
		int oldPos = currentMonster.getPosition();
		currentMonster.move(roll);
		int newPos = currentMonster.getPosition();

		if (newPos == opponentMonster.getPosition()) {
			currentMonster.setPosition(oldPos);
			throw new InvalidMoveException();
		}

		Cell landedCell = getCell(newPos);
		landedCell.onLand(currentMonster, opponentMonster);
		if (currentMonster.isConfused()) currentMonster.decrementConfusion();
		updateMonsterPositions(currentMonster, opponentMonster);
	}
	
	private void updateMonsterPositions(Monster player, Monster opponent){
		for(int i = 0; i<Constants.BOARD_SIZE;i++){
			Cell cell = getCell(i);
			if(cell.getMonster()!=null){
				cell.setMonster(null);
			}
		}
		Cell playerCell = getCell(player.getPosition());
		playerCell.setMonster(player);
		Cell opponentCell=getCell(opponent.getPosition());
		opponentCell.setMonster(opponent);
	}
}