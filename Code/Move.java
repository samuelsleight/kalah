public class Move
{
	private int playerID;
	private int move;
	
	public Move(int playerID, int move)
	{
		this.playerID = playerID;
		this.move = move;
	}
	
	public int getPlayerID()
	{
		return playerID;
	}
	
	public int getMove()
	{
		return move;
	}
}
