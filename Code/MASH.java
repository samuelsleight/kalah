import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

public class MASH extends AIBase //MASH Algorithm - Mega Autonomous Sexy Heuristic Algorithm
{
	private Map<GameState, ProbArray> memory;
	private ArrayList<CrappyPair> currentGame;
	private Random r;

	//TODO - 
	//searchy bit
	//heuristics vvvvvv
	//move that puts most in your pit - in their pit
	//piping

	private static final double PROB_DELTA = 0.2;

	MASH(KalahGame g, int playerID)
	{
		super(g, playerID);

		memory = new HashMap<GameState, ProbArray>();
		currentGame = new ArrayList<CrappyPair>();
		r = new Random();
		
	}

	public void win() {
		for(CrappyPair p : currentGame) {
			memory.get(p.state).updateProbability(p.choice, PROB_DELTA);
		}

		currentGame.clear(); 

	}

	public void lose() {
		for(CrappyPair p : currentGame) {
			memory.get(p.state).updateProbability(p.choice, -PROB_DELTA);
		}

		currentGame.clear(); 

	}

	public int makeMove()
	{
		int[] moves = game.getAllowedMoves(getPlayerID());
		GameState s = new GameState(moves, getPlayerID());
		if(!(memory.containsKey(s))) {
			double[] probs = new double[moves.length];
			for(int i = 0; i < moves.length; i++) 
			{
				probs[i] = 1.0 / (double)(moves.length);
			}

			memory.put(s, new ProbArray(probs));
		}

		ProbArray probs = memory.get(s);
		double val = r.nextDouble();
		double accum = 0;
		int i = 0;
		for(; i < probs.getSize() ; i++) //This looks so stupidly awesome ;)
		{
			accum += probs.get(i);
			if(val < accum) 
			{
				break;
			}
		}

		currentGame.add(new CrappyPair(s, i));
		return moves[i];
	}

	private Tree<KalahGame> createTree(int num, Tree<KalahGame> parentGame) //depth of ~5 to keep it manageable
	{
		Tree tree = new Tree(null, 1);
		if(num < 6) //depth
		{
			//TODO - give getData player ID
			//int[] moves = parentGame.getData().getAllowedMoves();
			Tree tree = new Tree(null, 1);
			for(int i = 0; i < moves.length; i++)
			{
				KalahGame newState = parentGame.getData().getState(moves[i]);
				Tree<KalahGame> t = new Tree<KalahGame>(parentGame, newState);

			}

			for(int i = 0; i < game.getAllowedMoves().length; i++)
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

		}//do pruning (via stricter heuristic?) here

		else
		{
			return tree; //yes, yes, a *real* tree needs to be made
		}
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

	public void updateProbability(int i, double d) {
		probs[i] += d;

		double total = 0;
		for(double p : probs) {
			total += p;

		}

		double factor = 1.0 / total;
		for(int j = 0; j < probs.length; j++) {
			probs[j] *= factor;

		}

	}
}

private class CrappyPair {
	public GameState state;
	public int choice;

	public CrappyPair(GameState state, int choice) {
		this.state = state;
		this.choice = choice;
	}

}

}
