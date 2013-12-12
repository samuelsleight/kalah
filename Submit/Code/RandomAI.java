import java.util.Random;
import java.util.ArrayList;

/**
A random AI
*/
public class RandomAI extends AIBase {
	private Random rand;
	
	/**
	Creates the Player
	@param game The game the player will be playing
	@param playerID The ID of the player (either PLAYER_1 or PLAYER_2)
	*/
	public RandomAI(KalahGame game, int playerID) {
		super(game, playerID);
		
		rand = new Random();
	}

	public void win() { }
	public void lose() { }
	
	ArrayList<Integer> toMove = new ArrayList<Integer>();
	
	public int makeMove()
	{
		if (toMove.isEmpty()) {
			ArrayList<int[]> moves = game.getAllowedMoveSequences(getPlayerID());
			
			int[] randMove = moves.get(rand.nextInt(moves.size()));
			
			for (int i = 0; i < randMove.length; i++) {
				toMove.add(randMove[i]);
			}
		}
		
		return toMove.remove(0);
	}
}
