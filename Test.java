import java.util.Scanner;

public class Test {
	private Scanner s;

	public Test() {
		s = new Scanner(System.in);

	}

	public void run() {
		KalahGame kg = new KalahGame();

		while(true) {
			System.out.println(kg);

			System.out.println("Sow from which house? ");
			int choice = getInt();

			try {
				kg.sow(choice);

			} catch(IllegalArgumentException e) {
				System.out.println(e.getMessage());

			}

		}

	}

	public int getInt() {
		int choice = 0;

		while(true) {
			if(s.hasNextInt()) {
				choice = s.nextInt(); s.nextLine();
				return choice;

			} else {
				s.next();

			}

		}

	}

	public static void main(String[] args) {
		Test t = new Test();
		t.run();

	}

}
