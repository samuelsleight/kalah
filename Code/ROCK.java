import java.util.ArrayList;

public class ROCK extends AIBase
{
	private ArrayList<Integer> toMove;
	
	private int maxDepth; 

	private Tree<int[]> prevMoveTree;
	
	public ROCK(KalahGame game, int playerID, int maxDepth)
	{
		super(game, playerID);
		
		this.maxDepth = maxDepth;
		
		toMove = new ArrayList<Integer>();
	}
	
	public int evaluate(KalahGame cur)
	{
		if (cur.getTurn() == KalahGame.PLAYER_1) {
			return cur.getSeeds(6) - cur.getSeeds(13);
		} else {
			return cur.getSeeds(13) - cur.getSeeds(6);
		}
	}

	int[] doThis;
	
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
	
	public void generateMoveSequence()
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
	}
	
	public int makeMove()
	{
		if (toMove.isEmpty()) {
			negaMax(game, maxDepth);
			
			for (int i = 0; i < doThis.length; i++) {
				toMove.add(doThis[i]);
			}
		}
		
		//System.out.println(game);
		//System.out.println("ROCK " + getPlayerID() + "'s move: " + java.util.Arrays.toString(getAllowedMoves()) + ": " + toMove.get(0));
		
		return toMove.remove(0);
	}
	
	private ArrayList<int[]> getMoveSequence()
	{
		ArrayList<Move> prevMoves = game.getPreviousMoves();
		
		int currentPlayer = game.getStartingPlayerID();
		ArrayList<Integer> currentMove = new ArrayList<Integer>();

		ArrayList<int[]> moveSequence = new ArrayList<int[]>();

		while (!prevMoves.isEmpty()) {
			Move move = prevMoves.remove(0);
			
			if (move.getPlayerID() == currentPlayer) {
				currentMove.add(move.getMove());
			} else {
				int[] moveArray = new int[currentMove.size()];

				for (int i = 0; i < currentMove.size(); i++) {
					moveArray[i] = currentMove.get(i);
				}
				
				moveSequence.add(moveArray);
				
				currentMove.clear();
			}
		}
		
		return moveSequence;
	}
	
	public void createTree()
	{
		
	}

	public void win()
	{
		
	}
	
	public void lose()
	{
	
	}
}
