import java.util.ArrayList;

public class ROCK extends AIBase
{
	ArrayList<Integer> toMove;
	
	public ROCK(KalahGame game, int playerID)
	{
		super(game, playerID);
		
		toMove = new ArrayList<Integer>();
		
		moo = 0;
	}
	
	private ArrayList<int[]> getMoves(KalahGame start, int player)
	{
		int[] moves = start.getAllowedMoves(player);
		
		ArrayList<int[]> frontier = new ArrayList<int[]>();
		ArrayList<int[]> finale = new ArrayList<int[]>();
		
		for (int i = 0; i < moves.length; i++) {
			KalahGame next = start.getState(moves[i]);
			
			int[] move = {moves[i]};
			
			if (next.getTurn() != player) {
				finale.add(move);
			} else {
				frontier.add(move);
			}
		}
		
		while (!frontier.isEmpty()) {
			ArrayList<int[]> newFrontier = new ArrayList<int[]>();
			
			for (int i = 0; i < frontier.size(); i++) {
				int[] moveSeq = frontier.get(i);
				
				KalahGame cur = start.getState(moveSeq);
				
				moves = cur.getAllowedMoves(player);
				
				if (moves.length == 0) {
					finale.add(moveSeq);
				}
				
				for (int moveNum = 0; moveNum < moves.length; moveNum++) {
					KalahGame next = cur.getState(moves[moveNum]);
					
					int[] newMoveSeq = new int[moveSeq.length + 1];
				
					for (int n = 0; n < moveSeq.length; n++) {
						newMoveSeq[n] = moveSeq[n];
					}
			
					newMoveSeq[moveSeq.length] = moves[moveNum];
						
					if (next.getTurn() != player) {
						finale.add(newMoveSeq);
					} else {
						newFrontier.add(newMoveSeq);
					}
				}
			}
			
			frontier = newFrontier;
		}
		
		return finale;
	}
	
	
	public int evaluate(KalahGame cur)
	{
		//return ((1.0 + cur.getSeeds(13)) / (1.0 + cur.getSeeds(6)));
		
		if (getPlayerID() == KalahGame.PLAYER_1) {
			return cur.getSeeds(6) - cur.getSeeds(13);
		} else {
			return cur.getSeeds(13) - cur.getSeeds(6);
		}
	}

	/*private int max(KalahGame cur, int depth)
	{
		if (depth == 0) {
			return evaluate(cur);
		}
		
		int max = Integer.MIN_VALUE;
		
		int[] moves = cur.getMoves(cur.getTurn());
		
		if (moves.length == 0) {
			return evaluate(cur);
		}
		
		for (int i = 0; i < moves.length; i++) {
			KalahGame next = cur.getState(moves[i]);
		
			int score;
			if (next.getTurn() == getPlayerID()) {
				score = max(next, depth - 1);
			} else {
				score = min(next, depth - 1);
			}
			
			if (score > max) {
				max = score;
			}
		}
		
		return max;
	}
	
	private int min(KalahGame cur, int depth)
	{
		if (depth == 0) {
			return evaluate(cur);
		}
		
		int min = Integer.MAX_VALUE;
		
		int[] moves = cur.getAllowedMoves(cur.getTurn());
		
		if (moves.length == 0) {
			return evaluate(cur);
		}
		
		for (int i = 0; i < moves.length; i++) {
			KalahGame next = cur.getState(moves[i]);
			
			int score;
			if (next.getTurn() == getPlayerID()) {
				score = max(next, depth - 1);
			} else {
				score = min(next, depth - 1);
			}
			
			if (score < min) {
				min = score;
			}
		}
		
		return min;
	}*/
	
	private int moo;
	
	private int negaMax(KalahGame cur, int depth)
	{
		if (depth == 0) {
			return evaluate(cur);
		}
		
		int max = Integer.MIN_VALUE;
		
		ArrayList<int[]> moves = getMoves(cur, cur.getTurn());
		
		if (moves.isEmpty()) {
			return evaluate(cur);
		}
		
		for (int i = 0; i < moves.size(); i++) {
			KalahGame next = cur.getState(moves.get(i));
			
			int score = -negaMax(next, depth - 1);
			
			if (score > max) {
				max = score;
				//toMove.clear();
				for (int x = 0; x < moves.get(i).length; x++) {
					//toMove.add(moves.get(i)[x]);///HERE IS WHWAT BNREAKS EVREYTHINBG
				}
			}
		}
		
		return max;
	}
	
		/*int maxi( int depth ) {
	    if ( depth == 0 ) return evaluate();
	    int max = -oo;
	    for ( all moves) {
		score = mini( depth - 1 );
		if( score > max )
		    max = score;
	    }
	    return max;
	}
	 
	int mini( int depth ) {
	    if ( depth == 0 ) return -evaluate();
	    int min = +oo;
	    for ( all moves) {
		score = maxi( depth - 1 );
		if( score < min )
		    min = score;
	    }
	    return min;
	}*/
	
	private int maxi(KalahGame cur, int depth)
	{
		if (depth == 0) {
			return evaluate(cur);
		}
		
		int max = Integer.MIN_VALUE;
		
		ArrayList<int[]> moves = getMoves(cur, cur.getTurn());
		
		if (moves.isEmpty()) {
			return evaluate(cur);
		}
		
		for (int i = 0; i < moves.size(); i++) {
			KalahGame next = cur.getState(moves.get(i));
			
			int score = mini(next, depth - 1);
			
			if (score > max) {
				max = score;
			}
		}
		
		return max;
	}
	
	private int mini(KalahGame cur, int depth)
	{
		if (depth == 0) {
			return evaluate(cur);
		}
		
		int min = Integer.MAX_VALUE;
		
		ArrayList<int[]> moves = getMoves(cur, cur.getTurn());
		
		if (moves.isEmpty()) {
			return evaluate(cur);
		}
		
		for (int i = 0; i < moves.size(); i++) {
			KalahGame next = cur.getState(moves.get(i));
			
			int score = maxi(next, depth - 1);
			
			if (score < min) {
				min = score;
			}
		}
		
		return min;
	}
	public int makeMove()
	{
		if (toMove.isEmpty()) {
			ArrayList<int[]> moves = getMoves(game, getPlayerID());
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
		
		System.out.println(game);
		System.out.println("Player " + getPlayerID() + "'s move: " + java.util.Arrays.toString(getAllowedMoves()) + ": " + toMove.get(0));
		
		return toMove.remove(0);
	}
	
	public void win()
	{
	
	}
	
	public void lose()
	{
	
	}
}
