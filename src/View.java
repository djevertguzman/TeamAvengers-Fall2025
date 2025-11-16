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
		case 1:
			drawMainMenu();
			return;
		case 2:
			drawNavigation();
			return;
		case 3:
			drawInventory();
			return;
		case 4:
			drawStats();
			return;
		case 5:
			drawPuzzle();
			return;
		case 6:
			drawMap();
			return;
		case 7:
			drawCombat();
			return;
		case 8:
			drawFight();
			return;
		default:
			System.out.println("Invalid View has been called.");
			return;
		}
	}
	public static void drawMainMenu() {
		System.out.println("---------Main Menu----------");
		printMessage();
		
	}
	public static void drawNavigation() {
		System.out.println("---------Navigation----------");
		printMessage();
	}
	public static void drawStats() {
		System.out.println("---------Statistics----------");
		printMessage();
	}
	public static void drawInventory() {
		System.out.println("---------Inventory----------");
		printMessage();
	}
	public static void drawCombat() {
		System.out.println("---------Monsters!----------");
		printMessage();
	}
	public static void drawFight() {
		System.out.println("---------Fight!----------");
		printMessage();
	}
	public static void drawPuzzle() {
		System.out.println("---------Quiz Time----------");
		printMessage();
	}
	public static void drawMap() {
		System.out.println("---------World Map----------");
		printMessage();
	}
	public static void printMessage() {
		if(!message.equals("")) {
			System.out.println(message);
			}
			message = "";
	}
}
