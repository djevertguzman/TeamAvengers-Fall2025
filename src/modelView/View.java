package modelView;
//Evert Guzman
public class View {
	private static StringBuilder message = new StringBuilder();
	private static int currView;
		
	public static void switchView(int x) {
		currView = x;
	}
	public static void setMessage(String incoming) {
		if (message.length() > 0) {
	        message.append("\n");          // add newline before the next message
	    }
	    message.append(incoming);
	}
	private static void clearMessage() {
		message.setLength(0); 
	}
	public static void draw() {
		clearScreen();
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
		case 5:
			drawMisc();
			return;
		default:
			System.out.println("Invalid View has been called.");
			return;
		}
	}
	private static void drawNavigation() {
		System.out.println("---------Navigation----------");
		System.out.println(message.toString());
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
		System.out.println("---------Zombies!----------");
		printMessage();
	}
	private static void drawPuzzle() {
		System.out.println("--------Puzzle Time---------");
		printMessage();
	}
	private static void drawMisc() {
		// Creating this to draw system messages without a banner. Will have the debug flag for now.
		System.out.println("--------Debug Time---------");
		printMessage();
	}
	private static void printMessage() {
		if(!message.equals("")) {
			System.out.println(message.toString());
			}
			clearMessage();
	}
	public static void clearScreen() {
	    for (int i = 0; i < 50; i++) {
	        System.out.println();
	    }
	}
}
