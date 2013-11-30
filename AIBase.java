public abstract class AIBase {
	protected KalahGame game;
	private int playerID;

	public AIBase(KalahGame game, int playerID) {
		this.game = game;
		this.playerID = playerID;

	}

	abstract void makeMove();
	
	/**
	Gets the legal moves a player can take
	@return The legal moves for that player
	*/
	public int[] getAllowedMoves()
	{
		return game.getAllowedMoves(playerID);
	}
	
	public int getPlayerID()
	{
		return playerID;
	}
}
