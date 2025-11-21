// Evert Guzman
package mainGame;

public class View {
	static int currView;
	static String message = "";
		
	public static void switchView(int x) {
		currView = x;
	}
	public static void setMessage(String incoming) {
		message = incoming;
	}
	private static void clearMessage() {
		message = "";
	}
	public static void draw() {
		switch(currView) {
		case 0:
			drawMainMenu();
			return;
		case 1:
			drawNavigation();
			return;
		case 2:
			drawInventory();
			return;
		case 3:
			drawStats();
			return;
		case 4:
			drawPuzzle();
			return;
		case 5:
			drawMap();
			return;
		case 6:
			drawCombat();
			return;
		case 7:
			drawFight();
			return;
		default:
			System.out.println("Invalid View has been called.");
			return;
		}
	}
	private static void drawMainMenu() {
		System.out.println("---------Main Menu----------");
		System.out.println("1. Start New Game [start]");
		System.out.println("2. Load Game [load]");
		System.out.println("3. Exit [exit]");
		System.out.println("4. Help [help]");
		System.out.println("------------------------------------------------------------------------------------------");
		System.out.print("Enter Selection []: ");
		printMessage();
		
	}
	private static void drawNavigation() {
		System.out.println("---------Navigation----------");
		printMessage();
	}
	private static void drawStats() {
		System.out.println("---------Statistics----------");
		printMessage();
	}
	private static void drawInventory() {
		System.out.println("---------Inventory----------");
		printMessage();
	}
	private static void drawCombat() {
		System.out.println("---------Monsters!----------");
		printMessage();
	}
	private static void drawFight() {
		System.out.println("---------Fight!----------");
		printMessage();
	}
	private static void drawPuzzle() {
		System.out.println("---------Quiz Time----------");
		printMessage();
	}
	private static void drawMap() {
		System.out.println("---------World Map----------");
		printMessage();
	}
	private static void printMessage() {
		if(!message.equals("")) {
			System.out.println(message);
			}
			clearMessage();
	}
}
