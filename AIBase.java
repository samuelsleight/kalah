public abstract class AIBase {
	private KalahGame game;

	public AIBase(KalahGame game) {
		this.game = game;

	}

	abstract void makeMove();

}
