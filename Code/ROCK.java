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
	
	private int negaMax(KalahGame cur, int depth)
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
	}
	
	/*public void generateMoveSequence()
	{
		ArrayList<int[]> moves = game.getAllowedMoveSequences(getPlayerID());
		int[] weights = new int[moves.size()];
	
		int selected = -1;
		int max = Integer.MIN_VALUE;
	
		for (int i = 0; i < moves.size(); i++) {
			KalahGame next = game.getState(moves.get(i));
			
			int result = evaluate(next);

			if (result > max) {
				max = result;
				selected = i;
			}
			weights[i] = result;
		}
		
		for (int i = 0; i < moves.get(selected).length; i++) {
			toMove.add(moves.get(selected)[i]);
		}
		
		for (int i = 0; i < moves.size(); i++) {
			System.out.println(java.util.Arrays.toString(moves.get(i)) + " : " + weights[i]);
		}
	}*/
	
	private void randomMove()
	{
		ArrayList<int[]> moves = game.getAllowedMoveSequences(getPlayerID());

		int randMove = rand.nextInt(moves.size());
		
		doThis = moves.get(randMove);
	}
	
	/*public void wingin()
	{
		ArrayList<int[]> prevMoves = getMoveSequence();
		
		// reset tree on AI's first move
		if (prevMoves.size() < 2) {
			iter = tree;
		}
		
		// move to other playrs move in tree (if it exists)
		if (!prevMoves.isEmpty()) {
			// other players previous move
			int[] moveSequence = prevMoves.get(prevMoves.size() - 1);
			
			boolean found = false;
			
			if (iter != null) {
				ArrayList<Tree<TreePair>> children = iter.getChildren();
		
				for (int c = 0; c < children.size(); c++) {
					Tree<TreePair> child = children.get(c);
			
					if (Arrays.equals(moveSequence, child.getData().moveSequence)) {
						iter = child;
						found = true;
				
						break;
					}
				}
			
				// if no path exists, just set the iterator to null
				if (!found) {
					iter = null;
				}
			}
		}
		
		if (iter == null) {
			randomMove();
			return;
		}
		
		ArrayList<Tree<TreePair>> children = iter.getChildren();
		
		if (children.isEmpty()) {
			randomMove();
		} else {
			double max = -1.0 / 0.0;
			int selected = -1;
			
			for (int c = 0; c < children.size(); c++) {
				Tree<TreePair> child = children.get(c);
				double weight = child.getData().weight;
				
				if (weight > max) {
					max = weight;
					selected = c;
				}
				
				//PRINT
				Tree<TreePair> printer = child;
				while (printer != null) {
					System.out.println(Arrays.toString(printer.getData().moveSequence));
					printer = printer.getParent();
				}
			}
			
			iter = children.get(selected);
			
			doThis = iter.getData().moveSequence;
		}
	}*/
	
	public void wingin()
	{
		ArrayList<int[]> prevMoves = getMoveSequence();
		
		// reset tree on AI's first move
		if (prevMoves.size() < 2) {
			iter = tree;
		}
		if (iter == null) {
			randomMove();
			return;
		}
		
		// move through the opponents move in the tree iterator
		if (!prevMoves.isEmpty()) {
			int[] moveSequence = prevMoves.get(prevMoves.size() - 1);
			
			boolean found = false;
			
			ArrayList<Tree<TreePair>> children = iter.getChildren();
	
			for (int c = 0; c < children.size(); c++) {
				Tree<TreePair> child = children.get(c);
		
				if (Arrays.equals(moveSequence, child.getData().moveSequence)) {
					iter = child;
					found = true;
					break;
				}
			}
		
			// if no path exists, just set the iterator to null
			if (!found) {
				iter = null;
				randomMove();
				return;
			}
		}
		
		ArrayList<Tree<TreePair>> children = iter.getChildren();
		
		if (children.isEmpty()) {
			randomMove();
		} else {
			// choose the best move (minimax perhaps)
			double max = -1.0 / 0.0;
			int selected = -1;
			
			for (int c = 0; c < children.size(); c++) {
				Tree<TreePair> child = children.get(c);
				double weight = child.getData().weight;
				
				boolean isValid = false;
				
				ArrayList<int[]> moves = game.getAllowedMoveSequences(getPlayerID());
				
				for (int i = 0; i < moves.size(); i++) {
					if (Arrays.equals(child.getData().moveSequence, moves.get(i))) {
						isValid = true;
					}
				}
				
				if (!isValid) {
					continue;
				}
				
				if (weight > max) {
					max = weight;
					selected = c;
				}
			}
			
			if (max == (-1.0 / 0.0)) {
				randomMove();
				return;
			}
			
			iter = children.get(selected);
			
			doThis = iter.getData().moveSequence;
		}
	}
	
	public int makeMove()
	{
		if (toMove.isEmpty()) {
			//negaMax(game, maxDepth);
			wingin();
			
			for (int i = 0; i < doThis.length; i++) {
				toMove.add(doThis[i]);
			}
		}
		
		//System.out.println(game);
		//System.out.println("ROCK " + getPlayerID() + "'s move: " + Arrays.toString(getAllowedMoves()) + ": " + toMove.get(0));
		
		return toMove.remove(0);
	}
	
	/**
	Converts the KalahGame's previous move list into a sequence of moves
	*/
	private ArrayList<int[]> getMoveSequence()
	{
		ArrayList<Move> prevMoves = game.getPreviousMoves();
		
		ArrayList<int[]> moveSequence = new ArrayList<int[]>();
		
		ArrayList<Integer> currentMoveSequence = new ArrayList<Integer>();
		
		int currentPlayer = game.getStartingPlayerID();
		
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
		
		return moveSequence;
	}
	
	public void createTree(ArrayList<int[]> prevMoves, double originalWeight)
	{
		Tree<TreePair> current = tree;
		
		int player = game.getStartingPlayerID();
		
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
				Tree newTree = new Tree<TreePair>(current, new TreePair(prevMoves.get(i), weight));
				
				current = newTree;
			}
			
			if (player == KalahGame.PLAYER_1) {
				player = KalahGame.PLAYER_2;
			} else {
				player = KalahGame.PLAYER_1;
			}
		}
		
		ArrayList<Tree<TreePair>> children = tree.getChildren();
		
		/*for (int c = 0; c < children.size(); c++) {
			Tree<TreePair> child = children.get(c);
			System.out.println(Arrays.toString(child.getData().moveSequence) + " : " + child.getData().weight);
		}*/
	}

	public void win()
	{
		ArrayList<int[]> sequence = getMoveSequence();
		
		/*for (int i = 0; i < sequence.size(); i++) {
			System.out.println(java.util.Arrays.toString(sequence.get(i)));
		}*/
		
		createTree(sequence, 1.0);
	}
	
	public void lose()
	{
		ArrayList<int[]> sequence = getMoveSequence();
		
		/*for (int i = 0; i < sequence.size(); i++) {
			System.out.println(java.util.Arrays.toString(sequence.get(i)));
		}*/
		
		createTree(sequence, -1.0);
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
