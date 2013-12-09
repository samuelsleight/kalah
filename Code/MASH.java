import java.util.Map;
import java.util.HashMap;
import java.util.Random;

public class MASH extends AIBase //MASH Algorithm - Mega Autonomous Sexy Heuristic Algorithm
{
	private Map<GameState, ProbArray> memory;
	private Random r;

	MASH(KalahGame g, int playerID)
	{
		super(g, playerID);

		memory = new HashMap<GameState, ProbArray>();
		r = new Random();
		
	}

	public int makeMove()
	{
		int[] moves = game.getAllowedMoves(getPlayerID());
		GameState s = new GameState(moves, getPlayerID());
		if(!(memory.containsKey(s))) {
			double[] probs = new double[moves.length];
			for(int i = 0; i < moves.length; i++) {
				probs[i] = 1.0 / (double)(moves.length);

			}

			memory.put(s, new ProbArray(probs));

		}

		ProbArray probs = memory.get(s);
		double val = r.nextDouble();
		double accum = 0;
		int i = 0;
		for(; i < probs.getSize(); i++) {
			accum += probs.get(i);
			if(val < accum) {
				break;
			}
		}

		return moves[i];
	}

	private Tree createTree()
	{
		Tree tree = new Tree(null, 1);
		for(int i = 0; i < 6; i++)
		{
			//sow

			if(true) //option is good continue searching - this is where we check by heuristic
			{
				//recursive call (?)
			}
			else
			{
				//set game back to current state
			}
		}

		//do pruning (via stricter heuristic?) here

		return tree; //yes, yes, a *real* tree needs to be made
	}

	private int addStochasticness() //can call elsewhere for bonus points, and as we know, points equate directly to prizes
	{
		return (int)(Math.random()*6); //can weight a certain house randomly
	}

	private void parseTree() //for useful information
	{
		
	}

	

private class GameState {
	private int[] availableMoves;
	private int player;

	public GameState(int[] availbleMoves, int player) {
		this.availableMoves = availableMoves;
		player = player;

	}

}

private class ProbArray {
	private double[] probs;

	public ProbArray(double[] probs) {
		this.probs = probs;

	}

	public int getSize() {
		return probs.length;

	}

	public double get(int i) {
		return probs[i];

	}
}

}
