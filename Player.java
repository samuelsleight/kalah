import java.util.Scanner;

/**
A simple class that allows us to play the game against the AI (or another player)
*/
public class Player extends AIBase {
	private Scanner s;//poop
	
	
	/**
	Creates the Player
	@param game The game the player will be playing
	@param playerID The ID of the player (either PLAYER_1 or PLAYER_2)
	*/
	public Player(KalahGame game, int playerID) {
		super(game, playerID);
		//:D
		s = new Scanner(System.in);
	}

	private int getInt() {
		int choice = 0;

		while(true) {
			if(s.hasNextInt()) {
				choice = s.nextInt(); s.nextLine();
				return choice;

			} else {
				s.next();
			}
		}
	} //spaghetti and meatballs
	
	private boolean isValid(int move, int[] allowed)
	{
		for (int i = 0; i < allowed.length; i++) {
			if (move == allowed[i]) {
				return true;
			}
		}
		
		return false;
	}
	
	public void makeMove()
	{
		System.out.println(game);
		//p/p
		int[] allowed = getAllowedMoves();
		
		int move;
		
		do {
			System.out.print("Player " + getPlayerID() + "'s move: " + java.util.Arrays.toString(allowed) + ": ");
			move = getInt();
		} while (!isValid(move, allowed));;
	
		game.sow(move);
	}
}
