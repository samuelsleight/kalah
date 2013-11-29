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
	private boolean player1;

	/**
	 * Constructs a new KalahGame, initialising the board to 4 seeds per house.
	 */
	public KalahGame() {
		board = new int[]{4, 4, 4, 4, 4, 4, 0, 4, 4, 4, 4, 4, 4, 0};
		player1 = true;

	}

	/**
	 * Sows the seeds from a given house. The house must not be empty, and must
	 * be on the correct player's side.
	 *
	 * @param n The house to sow the seeds from.
	 * @throws IllegalArgumentException if n is a store, on the wrong side of
	 * the board, or empty.
	 */
	public void sow(int n) {
		if(n == 6 || n == 13) {
			throw new IllegalArgumentException("Cannot sow seed from store (6 or 13)");

		} 

		if(board[n] == 0) {
			throw new IllegalArgumentException("Cannot sow seed from empty house");

		}

		if(player1) {
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
				player1 = false;

			}

			if(n < 6 && board[n] == 1) {
				board[6] += board[n];
				board[6] += board[14 - n];

				board[n] = 0;
				board[14 - n] = 0;

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
				player1 = true;

			}

			if(n > 6 && n < 13 && board[n] == 1) {
				board[13] += board[n];
				board[13] += board[14 - n];

				board[n] = 0;
				board[14 - n] = 0;

			}

		}

	}

	public String toString() {
		String str = "";

		for(int i = 13; i > 6; i--) {
			str += (board[i] + " ");
		}

		str += "\n  ";

		for(int i = 0; i < 7; i++) {
			str += (board[i] + " ");

		}

		return str;

	}

}
