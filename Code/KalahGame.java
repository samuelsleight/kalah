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
	private boolean player1ToMove;

	public static final int PLAYER_1 = 1;
	public static final int PLAYER_2 = 2;

	/**
	 * Constructs a new KalahGame, initialising the board to 4 seeds per house.
	 */
	public KalahGame() {
		board = new int[]{4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 0};
		player1ToMove = true;
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
	Changes the turn to the other player
	*/
	public void changeTurn()
	{
		player1ToMove = !player1ToMove;
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
			throw new IllegalArgumentException("Cannot sow seed from store (6 or 13)");

		} 

		if(board[n] == 0) {
			throw new IllegalArgumentException("Cannot sow seed from empty house");

		}

		if(player1ToMove) {
			if(n > 5) {
				throw new IllegalArgumentException("Seed must be sown from player 1s side (0-6)");

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
				throw new IllegalArgumentException("Seed must be sown from player 2s side (7-12)");

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
	Performs a players move, and checks if the game has ended
	@param player The player to move
	@return Returns true if the game has ended, otherwise returns false
	*/
	public boolean move(AIBase player)
	{
		sow(player.makeMove());
		
		int offset = (player.getPlayerID() - 1) * 7;
		
		boolean empty = true;
		
		// check if the board is empty
		for (int i = 0; i < 6; i++) {
			if (board[i + offset] > 0) {
				empty = false;
			}
		}
		
		return empty;
	}

	public int playGame(AIBase firstAI, AIBase secondAI)
	{
		boolean gameFinished = false;
	
		//main loop
		while(!gameFinished)
		{
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
