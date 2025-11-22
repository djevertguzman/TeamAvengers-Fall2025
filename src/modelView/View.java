package modelView;
//Evert Guzman
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
			drawNavigation();
			return;
		case 1:
			drawInventory();
			return;
		case 2:
			drawStats();
			return;
		case 3:
			drawPuzzle();
			return;
		case 4:
			drawCombat();
			return;
		default:
			System.out.println("Invalid View has been called.");
			return;
		}
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
	private static void drawPuzzle() {
		System.out.println("---------Quiz Time----------");
		printMessage();
	}
	private static void printMessage() {
		if(!message.equals("")) {
			System.out.println(message);
			}
			clearMessage();
	}
}
