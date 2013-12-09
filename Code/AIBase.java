public abstract class AIBase {
	protected KalahGame game;
	private int playerID;

	public AIBase(KalahGame game, int playerID) {
		this.game = game;
		this.playerID = playerID;

	}

	abstract int makeMove();

	abstract void win();
	abstract void lose();
	
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

	public void setPlayerID(int id)
	{
		playerID = id;
	}

}
