import java.util.Scanner;

public class Test {
	public static void main(String[] args) {
		KalahGame game = new KalahGame();

		/*
		AIBase firstPlayer = new Player(game, KalahGame.PLAYER_1);
		AIBase secondPlayer = new Player(game, KalahGame.PLAYER_2);
		*/

		AIBase firstPlayer = new MASH(game, KalahGame.PLAYER_1);
		AIBase secondPlayer = new MASH(game, KalahGame.PLAYER_2);
		
		//for(int i = 0; i < 1000; i++) {

		int result = game.playGame(firstPlayer, secondPlayer);
		
		System.out.println("FINAL STATE");
		System.out.println(game);
		
		switch (result) {
			case 0: System.out.println("Draw");		break;
			case 1: System.out.println("Player 1 Wins");	break;
			case 2: System.out.println("Player 2 Wins");	break;

		//}

		}
	}
}
