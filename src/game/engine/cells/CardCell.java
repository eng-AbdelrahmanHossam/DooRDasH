package game.engine.cells;

import game.engine.monsters.*;
import game.engine.Board;
import game.engine.cards.*;

public class CardCell extends Cell {
	
	public CardCell(String name) {
        super(name);
    }
	
	@Override
	public void onLand(Monster landingMonster, Monster opponentMonster){
		super.onLand(landingMonster, opponentMonster);
		Card card = Board.drawCard();
		card.performAction(landingMonster, opponentMonster);
	}
   
}
