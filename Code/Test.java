import java.util.Scanner;

public class Test {
	private static void pit(KalahGame game, AIBase firstPlayer, AIBase secondPlayer)
	{
		int player1Wins = 0;
		int player2Wins = 0;
		int draws = 0;
		
		for (int i = 0; i < 500; i++) {
			System.out.println("\n### Game " + (i + 1) + " ###\n");
			
			game.reset((i % 2) == 0 ? KalahGame.PLAYER_1 : KalahGame.PLAYER_2);
			
			int result = game.playGame(firstPlayer, secondPlayer);
			
			if (result == 1) {
				System.out.println("Player 1 won : ");
			} else if (result == 2) {
				System.out.println("Player 2 won : ");
			}
			
			switch (result) {
				case 0: draws++;	break;
				case 1: player1Wins++; firstPlayer.win(); secondPlayer.lose();	break;
				case 2: player2Wins++; firstPlayer.lose(); secondPlayer.win();	break;
			}
			
			//System.out.println(game);
		}
		
		System.out.println("---Results---");
		System.out.println("Player 1 won " + player1Wins + " game(s)");
		System.out.println("Player 2 won " + player2Wins + " game(s)");
		System.out.println("Both players drew " + draws + " time(s)");
	}

	public static void main(String[] args) {
		KalahGame game = new KalahGame(KalahGame.PLAYER_1);
		
		/*
		AIBase firstPlayer = new Player(game, KalahGame.PLAYER_1);
		AIBase secondPlayer = new ROCK(game, KalahGame.PLAYER_2);
		*/

		AIBase firstPlayer = new MASH(game, KalahGame.PLAYER_1);
		AIBase secondPlayer = new ROCK(game, KalahGame.PLAYER_2, 2);
		
		pit(game, firstPlayer, secondPlayer);

		/*
		MASH test = new MASH(game, KalahGame.PLAYER_1);
		
		Tree<KalahGame> t = new Tree<KalahGame>(null, game);
		test.createTree(1, t);
		System.out.println(t);
		*/
	}
}
