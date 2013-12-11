import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;
import java.util.Collections;
import java.util.Comparator;

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

	private static final double PROB_DELTA = 0.25;
	private static final double TREE_DEPTH = 7;

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
			Tree<KalahGame> t = new Tree<KalahGame>(null, game);
			createTree(0, t);

			ArrayList<IntPair> hvals = new ArrayList<IntPair>();
			for(int i = 0; i < t.getChildren().size(); i++) {
				hvals.add(new IntPair(i, calculateHeuristic(t.getChildren().get(i))));
			}

			Collections.sort(hvals, new Comparator<IntPair>() {
				public int compare(IntPair p1, IntPair p2) {
					return p1.compareTo(p2);

				}
			});

			double[] probs = new double[hvals.size()];
			probs[hvals.get(0).i1] = (double)(hvals.get(0).i2 ^ 2);

			for(int i = 1; i < hvals.size() - 1; i++) {
				probs[hvals.get(i).i1] = (double)(hvals.get(i).i2);

			}

			probs[hvals.get(hvals.size() - 1).i1] = Math.sqrt((double)(hvals.get(hvals.size() - 1).i2));

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

		i = (i >= moves.length ? i - 1 : i);
		currentGame.add(new CrappyPair(s, i));
		return moves[i];
	}

	private void createTree(int num, Tree<KalahGame> parentGame) //depth of ~5 to keep it manageable
	{
		if(num < TREE_DEPTH) //depth
		{
			int[] moves = parentGame.getData().getAllowedMoves(parentGame.getData().getTurn());

			//Tree<KalahGame> tree = new Tree<KalahGame>(null, 1);
			for(int i = 0; i < moves.length; i++)
			{
				createTree(num + 1, new Tree<KalahGame>(parentGame, parentGame.getData().getState(moves[i])));

			}

		}

	}

	private int calculateHeuristic(Tree<KalahGame> tree) {
		if(tree.getChildren().isEmpty()) {
			return heuristic(tree.getData());

		} else {
			int currMax = 0;
			for(int i = 0; i < tree.getChildren().size(); i++) {
				currMax = Math.max(currMax, calculateHeuristic(tree.getChildren().get(i)));

			}

			return currMax;
		}
	}

	private int heuristic(KalahGame g)
	{
		if(getPlayerID() == KalahGame.PLAYER_1) {
			return g.getSeeds(6) - g.getSeeds(13);

		} else {
			return g.getSeeds(13) - g.getSeeds(6);

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

		normalise();

	}

	public int getSize() {
		return probs.length;

	}

	public double get(int i) {
		return probs[i];

	}

	private void normalise() {
		double total = 0;
		for(double p : probs) {
			total += p;

		}

		double factor = 1.0 / total;
		for(int j = 0; j < probs.length; j++) {
			probs[j] *= factor;

		}

	}

	public void updateProbability(int i, double d) {
		probs[i] += d;

		normalise();

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

private class IntPair implements Comparable {
	public int i1, i2;

	public IntPair(int i1, int i2) {
		this.i1 = i1;
		this.i2 = i2;
	}

	public int compareTo(Object o) {
		IntPair other = (IntPair)o;
		if(i2 < other.i2) {
			return -1;

		} else if(i2 == other.i2) {
			return 0;

		} else {
			return 1;

		}
	}

}

}
