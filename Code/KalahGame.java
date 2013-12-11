import java.util.ArrayList;

/**
 * This class represents the game of Kalah.
 *
 * The board is represented as an array, indexed in a circle:
<pre>
{@code
13 12 11 10  9  8  7
    0  1  2  3  4  5  6}
</pre>
 * where 6 is player 1's store, and 13 is player 2's. 
 */
public class KalahGame {
	private int[] board;
	
	private boolean player1Started;
	private boolean player1ToMove;
	
	private ArrayList<Move> prevMoves;
	
	public static final int PLAYER_1 = 1;
	public static final int PLAYER_2 = 2;

	/**
	 * Constructs a new KalahGame, initialising the board to 4 seeds per house.
	 */
	public KalahGame(int startingPlayer) {
		prevMoves = new ArrayList<Move>();
		reset(startingPlayer);
	}

	public void reset(int startingPlayer) {
		if (startingPlayer != PLAYER_1 && startingPlayer != PLAYER_2) {
			throw new IllegalArgumentException("The starting player must be PLAYER_1 or PLAYER_2");
		
		}
		
		board = new int[]{4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 0};
		
		player1Started = startingPlayer == PLAYER_1;
		player1ToMove = player1Started;
		
		prevMoves.clear();
	}
	
	/**
	Gets the legal moves a player can take
	@param player The player (either 1 or 2)
	@return The legal moves for that player
	*/
	public int[] getAllowedMoves(int player)
	{
		if(player != PLAYER_1 && player != PLAYER_2) {
			throw new IllegalArgumentException("PlayerID is not valid: " + player);
		}
		
		int offset = (player - 1) * 7;
		
		int size = 0;
		
		//get the number of legal moves
		for (int i = offset; i < offset + 6; i++) {
			if (board[i] > 0) {
				size++;
			}
		}
		
		int[] moves = new int[size];
		int index = 0;
		
		//add the moves to the array
		for (int i = offset; i < offset + 6; i++) {
			if (board[i] > 0) {
				moves[index] = i;
				index++;
			}
		}
		
		return moves;
	}
	
	/**
	 * Returns an ArrayList of the possible move sequences a player can make on a turn.
	 * */
	public ArrayList<int[]> getAllowedMoveSequences(int player)
	{
		int[] moves = getAllowedMoves(player);
		
		ArrayList<int[]> frontier = new ArrayList<int[]>();
		ArrayList<int[]> finale = new ArrayList<int[]>();
		
		for (int i = 0; i < moves.length; i++) {
			KalahGame next = getState(moves[i]);
			
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
				
				KalahGame cur = getState(moveSeq);
				
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
	
	public int getStartingPlayerID()
	{
		return player1Started ? PLAYER_1 : PLAYER_2;
	}
	
	public ArrayList<Move> getPreviousMoves()
	{
		return prevMoves;
	}

	public int getTurn()
	{
		if (player1ToMove) {
			return PLAYER_1;
		} else {
			return PLAYER_2;
		}
	}
	
	/**
	Changes the turn to the other player
	*/
	public void changeTurn()
	{
		player1ToMove = !player1ToMove;
	}
	
	/**
	Returns the player that started
	*/
	public int playerStarted()
	{
		if (prevMoves.isEmpty()) {
			return player1ToMove ? PLAYER_1 : PLAYER_2;
		} else {
			return prevMoves.get(0).getPlayerID();
		}
	}
	
	public KalahGame getState(int move)
	{
		KalahGame next = new KalahGame(player1Started ? PLAYER_1 : PLAYER_2);
		
		for (int i = 0; i < prevMoves.size(); i++) {
			next.move(prevMoves.get(i).getMove());
		}
		
		next.move(move);
		
		return next;
	}
	
	public KalahGame getState(int[] moves)
	{
		KalahGame next = new KalahGame(player1Started ? PLAYER_1 : PLAYER_2);
		
		for (int i = 0; i < prevMoves.size(); i++) {
			next.move(prevMoves.get(i).getMove());
		}
		
		for (int i = 0; i < moves.length; i++) {
			next.move(moves[i]);
		}
		
		return next;
	}
	
	public int[] flipToPlayer1(int[] moveSequence)
	{
		if (player1Started) {
			return moveSequence;
		}
		
		int[] newMoveSequence = new int[moveSequence.length];
		
		for (int i = 0; i < newMoveSequence.length; i++) {
			if (moveSequence[i] < 6) {
				newMoveSequence[i] = moveSequence[i] + 7;
			} else {
				newMoveSequence[i] = moveSequence[i] - 7;
			}
		}
		
		return newMoveSequence;
	}
	
	/**
	 * Sows the seeds from a given house. The house must not be empty, and must
	 * be on the correct player's side.
	 *
	 * @param n The house to sow the seeds from.
	 * @throws IllegalArgumentException if n is a store, on the wrong side of
	 * the board, or empty.
	 */
	private void sow(int n) {
		if(n == 6 || n == 13) {
			throw new IllegalArgumentException("Cannot sow seed from store (6 or 13): " + n);

		} 

		if(board[n] == 0) {
			throw new IllegalArgumentException("Cannot sow seed from empty house: " + n);

		}

		if(player1ToMove) {
			if(n > 5) {
				throw new IllegalArgumentException("Seed must be sown from player 1s side (0-6): " + n);

			}

			int seeds = board[n];
			board[n] = 0;
			while(seeds > 0) {
				n++;
				if(n > 12) {
					n = 0;

				}

				board[n]++;
				seeds--;

			}

			if(n != 6) {
				changeTurn();

			}

			if(n < 6 && board[n] == 1) {
				int opposite = 12 - n;
				
				if (board[opposite] > 0) {
					board[6] += board[n];
					board[6] += board[opposite];

					board[n] = 0;
					board[opposite] = 0;
				}
			}

		} else {
			if(n < 7) {
				throw new IllegalArgumentException("Seed must be sown from player 2s side (7-12): " + n);

			}

			int seeds = board[n];
			board[n] = 0;
			while(seeds > 0) {
				n++;
				if(n > 13) {
					n = 0;

				}

				if(n == 6) {
					n++;

				}

				board[n]++;
				seeds--;

			}

			if(n != 13) {
				changeTurn();

			}

			if(n > 6 && n < 13 && board[n] == 1) {
				int opposite = 12 - n;
				
				if (board[opposite] > 0) {
					board[13] += board[n];
					board[13] += board[opposite];

					board[n] = 0;
					board[opposite] = 0;
				}
			}

		}

	}
	
	/**
	Gets the number of seeds in a specified pot
	*/
	public int getSeeds(int pot)
	{
		return board[pot];
	}
	
	/**
	Add any remaining pieces to the corresponding pot
	*/
	private void finish()
	{
		for (int i = 0; i < 6; i++) {
			board[6] += board[i];
			board[i] = 0;
		}
		for (int i = 7; i < 13; i++) {
			board[13] += board[i];
			board[i] = 0;
		}
	}
	
	/**
	Performs a players move, and checks if the game has ended
	@param player The player to move
	@return Returns true if the game has ended, otherwise returns false
	*/
	public boolean move(AIBase player)
	{
		int move = player.makeMove();
		
		sow(move);
		
		prevMoves.add(new Move(player.getPlayerID(), move));
		
		boolean empty = true;
		
		// check if any sides are empty
		for (int i = 0; i < 6; i++) {
			if (board[i] > 0) {
				empty = false;
			}
		}
		if (empty) {
			finish();
			return true;
		}
		
		empty = true;
		
		for (int i = 7; i < 13; i++) {
			if (board[i] > 0) {
				empty = false;
			}
		}
		if (empty) {
			finish();
			return true;
		}
		
		return false;
	}
	
	private boolean move(int move)
	{
		sow(move);
		prevMoves.add(new Move(player1ToMove ? PLAYER_1 : PLAYER_2, move));

		boolean empty = true;
		
		// check if any sides are empty
		for (int i = 0; i < 6; i++) {
			if (board[i] > 0) {
				empty = false;
			}
		}
		if (empty) {
			finish();
			return true;
		}
		
		empty = true;
		
		for (int i = 7; i < 13; i++) {
			if (board[i] > 0) {
				empty = false;
			}
		}
		if (empty) {
			finish();
			return true;
		}
		
		return false;
	}

	public int playGame(AIBase firstAI, AIBase secondAI)
	{
		boolean gameFinished = false;
	
		//main loop
		while(!gameFinished)
		{
			//System.out.println("Player: " + getTurn());
			//System.out.println(this);
			
			if (player1ToMove) {
				gameFinished = move(firstAI);
			} else {
				gameFinished = move(secondAI);
			}
		}
		
		//add remaining pieces to the corresponding side
		for (int i = 0; i < 6; i++) {
			board[6] += board[i];
			board[i] = 0;
		}
		for (int i = 7; i < 13; i++) {
			board[13] += board[i];
			board[i] = 0;
		}
		
		if(board[6] > board[13]) {
			return 1; //AI 1 wins
		} else if(board[13] > board[6]) {
			return 2; //AI 2 wins
		} else {
			return 0; //draw
		}
	}

	public String toString() {
		String str = "";

		for(int i = 13; i > 6; i--) {
			//add extra padding if necessary
			if (board[i] < 10) {
				str += " ";
			}
			
			str += (board[i] + " ");
		
		}

		str += "\n   ";

		for(int i = 0; i < 7; i++) {
			//add extra padding if necessary
			if (board[i] < 10) {
				str += " ";
			}
			
			str += (board[i] + " ");
		}

		return str;

	}
}
