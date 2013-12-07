
public class MASH extends AIBase //MASH Algorithm - Mega Autonomous Sexy Heuristic Algorithm
{
	private KalahGame currentState;

	MASH(KalahGame g, int playerID)
	{
		super(g, playerID);
		
	}

	public int makeMove()
	{
		//dostuff
		return 0;
	}

	private Tree createTree()
	{
		Tree tree = new Tree(null, 1);
		for(int i = 0; i < 6; i++)
		{
			//sow

			if(true) //option is good continue searching - this is where we check by heuristic
			{
				//recursive call (?)
			}
			else
			{
				//set game back to current state
			}
		}

		//do pruning (via stricter heuristic?) here

		return tree; //yes, yes, a *real* tree needs to be made
	}

	private int addStochasticness() //can call elsewhere for bonus points, and as we know, points equate directly to prizes
	{
		return (int)(Math.random()*6); //can weight a certain house randomly
	}

	private void parseTree() //for useful information
	{
		
	}

	
}
