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
		
		for(int i = 0; i < 1000; i++) {
			game.reset();

			int result;
			if(i % 2 == 0) {
				firstPlayer.setPlayerID(KalahGame.PLAYER_1);
				secondPlayer.setPlayerID(KalahGame.PLAYER_2);
				result = game.playGame(firstPlayer, secondPlayer);
			} else {
				firstPlayer.setPlayerID(KalahGame.PLAYER_2);
				secondPlayer.setPlayerID(KalahGame.PLAYER_1);
				result = game.playGame(secondPlayer, firstPlayer);

			}
		
			System.out.println("FINAL STATE");
			System.out.println(game);
		
			switch (result) {
				case 0: System.out.println("Draw");		break;
				case 1: firstPlayer.win(); secondPlayer.lose(); System.out.println("Player 1 Wins");	break;
				case 2: firstPlayer.lose(); secondPlayer.win(); System.out.println("Player 2 Wins");	break;

			}

		}
	}
}
