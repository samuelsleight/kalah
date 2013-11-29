public class AIBase {
	private KalahGame game;

	public AIBase(KalahGame g) {
		this.g = g;

	}

	abstract void makeMove();

}
