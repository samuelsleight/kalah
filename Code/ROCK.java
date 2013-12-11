import java.util.Arrays;
import java.util.ArrayList;
import java.util.Random;

public class ROCK extends AIBase
{
	private ArrayList<Integer> toMove;
	
	private int maxDepth;

	private Tree<TreePair> tree;
	private Tree<TreePair> iter;

	private Random rand;

	private int[] doThis;
	
	public ROCK(KalahGame game, int playerID, int maxDepth)
	{
		super(game, playerID);
		
		this.maxDepth = maxDepth;
		
		toMove = new ArrayList<Integer>();
		
		tree = new Tree<TreePair>(null, new TreePair(null, 0.0));
		iter = tree;
		
		rand = new Random();
		
		doThis = null;
	}
	
	public int evaluate(KalahGame cur)
	{
		if (cur.getTurn() == KalahGame.PLAYER_1) {
			return cur.getSeeds(6) - cur.getSeeds(13);
		} else {
			return cur.getSeeds(13) - cur.getSeeds(6);
		}
	}
	
	private void randomMove()
	{
		ArrayList<int[]> moves = game.getAllowedMoveSequences(getPlayerID());

		int randMove = rand.nextInt(moves.size());
		
		doThis = moves.get(randMove);
	}
	
	/**
	Converts the KalahGame's previous move list into a sequence of moves
	*/
	private ArrayList<int[]> getMoveSequence(KalahGame cur)
	{
		ArrayList<Move> prevMoves = cur.getPreviousMoves();
		
		ArrayList<int[]> moveSequence = new ArrayList<int[]>();
		
		ArrayList<Integer> currentMoveSequence = new ArrayList<Integer>();
		
		int currentPlayer = cur.getStartingPlayerID();
		
		for (int i = 0; i < prevMoves.size(); i++) {
			Move move = prevMoves.get(i);
			
			if (move.getPlayerID() == currentPlayer) {
				currentMoveSequence.add(move.getMove());
			} else {
				currentPlayer = move.getPlayerID();
				
				// convert moveSequence into an int array
				int[] moveArray = new int[currentMoveSequence.size()];
				
				for (int x = 0; x < moveArray.length; x++) {
					moveArray[x] = currentMoveSequence.get(x);
				}
				
				moveSequence.add(moveArray);
				
				currentMoveSequence.clear();
				
				currentMoveSequence.add(move.getMove());
			}
		}
		
		//add any remaining moves
		if (!currentMoveSequence.isEmpty()) {
			// convert moveSequence into an int array
			int[] moveArray = new int[currentMoveSequence.size()];
			
			for (int x = 0; x < moveArray.length; x++) {
				moveArray[x] = currentMoveSequence.get(x);
			}
			
			moveSequence.add(moveArray);
		}
		
		return moveSequence;
	}
	
	// minimax part of the AI
	
	/*private int negaMax(KalahGame cur, int depth)
	{
		if (depth == 0) {
			return evaluate(cur);
		}
		
		int max = Integer.MIN_VALUE;
		
		ArrayList<int[]> moves = cur.getAllowedMoveSequences(cur.getTurn());
		int[] bestMove = {};
				
		if (moves.isEmpty()) {
			return evaluate(cur);
		}
		
		for (int i = 0; i < moves.size(); i++) {
			KalahGame next = cur.getState(moves.get(i));
			
			int score = -negaMax(next, depth - 1);
			
			if (score > max) {
				max = score;
				bestMove = moves.get(i);
			}
		}
		
		doThis = bestMove;
		
		return max;
	}*/
	
	private int mini(KalahGame cur, int depth)
	{
		if (depth <= 0) {
			return evaluate(cur);
		}
		
		int min = Integer.MAX_VALUE;
	
		ArrayList<int[]> moves = cur.getAllowedMoveSequences(cur.getTurn());
		
		if (moves.isEmpty()) {
			return evaluate(cur);
		}
		
		int[] bestMove = {};
		
		for (int i = 0; i < moves.size(); i++) {
			KalahGame next = cur.getState(moves.get(i));
			
			int score = maxi(next, depth - 1);
			
			if (score < min) {
				min = score;
				bestMove = moves.get(i);
			}
		}
		
		doThis = bestMove;
		
		return min;
	}
	
	private int maxi(KalahGame cur, int depth)
	{
		if (depth <= 0) {
			return evaluate(cur);
		}
		
		int max = Integer.MIN_VALUE;
	
		ArrayList<int[]> moves = cur.getAllowedMoveSequences(cur.getTurn());
		
		if (moves.isEmpty()) {
			return evaluate(cur);
		}
		
		int[] bestMove = {};
		
		for (int i = 0; i < moves.size(); i++) {
			KalahGame next = cur.getState(moves.get(i));
			
			int score = mini(next, depth - 1);
			
			if (score > max) {
				max = score;
				bestMove = moves.get(i);
			}
		}
		
		doThis = bestMove;
		
		return max;
	}
	
	// learning part of the AI
	
	public double[] learning()
	{
		ArrayList<int[]> prevMoves = getMoveSequence(game);
		
		//System.out.println("PREV:" + prevMoves.size());
		
		//reset tree on first move
		if (prevMoves.size() < 2) {
			iter = tree;
		}
		
		if (iter == null) {
			////randomMove();
			return null;
		}
		
		// move through the opponents move in the tree iterator
		if (!prevMoves.isEmpty()) {
			int[] moveSequence = prevMoves.get(prevMoves.size() - 1);
			
			moveSequence = game.flipToPlayer1(moveSequence);
			
			boolean found = false;
			
			//System.out.println("\nNot sure: " + Arrays.toString(moveSequence));
			//printTree(iter, 2);
			
			ArrayList<Tree<TreePair>> children = iter.getChildren();
	
			for (int c = 0; c < children.size(); c++) {
				Tree<TreePair> child = children.get(c);
		
				if (Arrays.equals(moveSequence, child.getData().moveSequence)) {
					iter = child;
					found = true;
					//System.out.println("Passed it on to " + Arrays.toString(child.getData().moveSequence));
					break;
				}
			}
		
			// if no path exists, just set the iterator to null
			if (!found) {
				iter = null;
				////randomMove();
				//System.out.println("Never passed");
				return null;
			}
		}
		
		ArrayList<Tree<TreePair>> children = iter.getChildren();
		
		if (children.isEmpty()) {
			////randomMove();
			return null;
		} else {
			// choose the best move (minimax perhaps)
			double max = 0.0;
			int selected = -1;
			
			double[] weightArray = new double[children.size()];
			
			for (int c = 0; c < children.size(); c++) {
				Tree<TreePair> child = children.get(c);
				double weight = child.getData().weight;// + rand.nextDouble() * 0.1;
				
				weightArray[c] = weight;
				
				if (weight > max) {
					max = weight;
					selected = c;
				}
			}
			
			// if all of the moves had a negative hei
			if (selected == -1) {
				
				////randomMove();
				return null;
			}
			
			if (max <= 0.0) {
				return null;
			}
			
			//System.out.print("Following previous Path: ");
			
			iter = children.get(selected);
			
			//System.out.println(Arrays.toString(iter.getData().moveSequence));
			//printTree(iter, 3);
			doThis = iter.getData().moveSequence;
			
			if (getPlayerID() == KalahGame.PLAYER_1 && doThis[0] > 6) {
				doThis = game.flipToPlayer1(doThis);
			} else  if (getPlayerID() == KalahGame.PLAYER_2 && doThis[0] < 7) {
				doThis = game.flipToPlayer1(doThis);
			}
			
			return weightArray;
			
			//System.out.println(Arrays.toString(doThis));
		}
	}
	
	
	
	public void generateTree(KalahGame cur, double originalWeight, boolean endgameCalculation)
	{
		ArrayList<int[]> originalPrevMoves = getMoveSequence(cur);
		
		// flip the tree if PLAYER_2 started
		ArrayList<int[]> prevMoves;
		
		if (cur.getStartingPlayerID() == KalahGame.PLAYER_1) {
			prevMoves = originalPrevMoves;
		} else {
			prevMoves = new ArrayList<int[]>(originalPrevMoves.size());
			
			// create opposite prev moves
			for (int i = 0; i < originalPrevMoves.size(); i++) {
				prevMoves.add(cur.flipToPlayer1(originalPrevMoves.get(i)));
			}
		}
		
		if (endgameCalculation) {
			// apply the algorithm to the endgame
			// playout some endgame scenarios at that place
		
			//play(prevMoves);
			return;
		}
		
		Tree<TreePair> current = tree;
		
		int player = cur.getStartingPlayerID();
		
		for (int i = 0; i < prevMoves.size(); i++) {
			double weight;
			
			if (player == getPlayerID()) {
				weight = originalWeight;
			} else {
				weight = -originalWeight;
			}
			
			int[] moveSequence = prevMoves.get(i);
			
			ArrayList<Tree<TreePair>> children = current.getChildren();
			
			boolean found = false;
			
			for (int c = 0; c < children.size(); c++) {
				Tree<TreePair> child = children.get(c);
				
				if (Arrays.equals(child.getData().moveSequence, prevMoves.get(i))) {
					current = child;
					
					TreePair pair = current.getData();
					
					pair.weight += weight;
					
					current.setData(pair);
					
					found = true;
					break;
				}
			}
			
			if (!found) {
				Tree<TreePair> newTree = new Tree<TreePair>(current, new TreePair(prevMoves.get(i), weight));
				
				current = newTree;
			}
			
			if (player == KalahGame.PLAYER_1) {
				player = KalahGame.PLAYER_2;
			} else {
				player = KalahGame.PLAYER_1;
			}
		}
	}
	
	/*void play(ArrayList<int[]> prevMoves)
	{
		//replay previous moves upto a point
		KalahGame cur = new KalahGame(game.getStartingPlayerID());
		
		//remove last 3 moves
		for (int i = 0; i < 1; i++) {
			if (prevMoves.isEmpty()) {
				break;
			}
			prevMoves.remove(prevMoves.size() - 1);
		}
		
		for (int i = 0; i < prevMoves.size(); i++) {
			int[] move = prevMoves.get(i);
		
			cur = cur.getState(move);
		}
		
		//
		
		ArrayList<KalahGame> frontier = new ArrayList<KalahGame>();
		
		ArrayList<int[]> moves = cur.getAllowedMoveSequences(cur.getTurn());
				
		for (int i = 0; i < moves.size(); i++) {
			frontier.add(cur.getState(moves.get(i)));
		}
		
		int iterations = 0;
		
		while (!frontier.isEmpty() && iterations++ < 10) {
			ArrayList<KalahGame> newFrontier = new ArrayList<KalahGame>();
			
			for (int i = 0; i < frontier.size(); i++) {
				KalahGame next = frontier.get(i);
				
				moves = next.getAllowedMoveSequences(next.getTurn());
				
				if (moves.isEmpty()) {
					//get score
					int score = cur.getSeeds(6) - cur.getSeeds(13);
					
					if (score != 0) {
						//add path to tree
						generateTree(next, score > 0 ? 1.0 : -1.0, false);
					}
				} else {
					for (int j = 0; j < moves.size(); j++) {
						newFrontier.add(next.getState(moves.get(j)));
					}
				}
			}
			
			frontier = newFrontier;
		}
		
		//generateTree(ArrayList<int[]> originalPrevMoves, 0.1, false);
	}*/
	
	private int getNodeDepth(Tree<TreePair> cur)
	{
		int depth = 0;
		
		while (cur.getParent() != null) {
			cur = cur.getParent();
			depth++;
		}
		
		return depth;
	}
	
	public void printTree(Tree<TreePair> cur, int depth)
	{	
		if (depth == 0) {
			return;
		}
		
		System.out.println("Depth " + depth + " = " + Arrays.toString(cur.getData().moveSequence));
		
		ArrayList<Tree<TreePair>> children = cur.getChildren();
		
		for (int i = 0; i < children.size(); i++) {
			printTree(children.get(i), depth - 1);
		}
	}
	
	/**
	The heart of the AI, where a sequence of moves is generated
	*/
	public int makeMove()
	{
		if (toMove.isEmpty()) {
			if (maxDepth > 0) {
				maxi(game, maxDepth);
			} else {
				double[] weights = learning();
				
				if (weights == null) {
					/*randomMove();*/maxi(game, 4);
				}
			}
			
			for (int i = 0; i < doThis.length; i++) {
				toMove.add(doThis[i]);
			}
		}
		
		//System.out.println(game);
		//System.out.println("ROCK " + getPlayerID() + "'s move: " + Arrays.toString(getAllowedMoves()) + ": " + toMove.get(0));
		
		//System.out.println("ROCK's move" + toMove.get(0));
		
		//printTree(tree, 5);
		
		return toMove.remove(0);
	}
	
	public void win()
	{
		ArrayList<int[]> sequence = getMoveSequence(game);
		
		generateTree(game, 1.0, false);
	}
	
	public void lose()
	{
		ArrayList<int[]> sequence = getMoveSequence(game);
		
		generateTree(game, -1.0, false);
	}
	
	private class TreePair
	{
		public int[] moveSequence;
		public double weight;
	
		public TreePair(int[] moveSequence, double weight)
		{
			this.moveSequence = moveSequence;
			this.weight = weight;
		}
	}
}
