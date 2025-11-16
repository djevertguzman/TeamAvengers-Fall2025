public class Main {
	public static void main(String[] args) {
		//I guess file load operations will go here.
		//At the moment I will create objects manually.
		System.out.println("Game Start!");
		View.switchView(0);
		Controller.switchControllerContext(0);
		View.draw();
		Controller.parseUSRInput();
		
	}
}
