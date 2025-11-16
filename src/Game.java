	import java.util.ArrayList;
	import Controller;
	import View;
	import newView;

	import java.util.Iterator;
	//Main game logic will go here.

	public class Game {
		static ArrayList<Room> Area = new ArrayList<Room>();
		static ArrayList<Puzzle> Puzzles = new ArrayList<Puzzle>();
		//static Combat fight;
		static boolean skipFight = false;
		static boolean fightOver = false;
		
		public static void addRoom(Room x) {
			Area.add(x);
			//System.out.println(x.getRoomName() + ": Has been loaded into the game");
		}
		public static void addPuzzle(Puzzle x) {
			Puzzles.add(x);
			//System.out.println(x.getRoomName() + ": Has been loaded into the game");
		}
		public static void newGame() {
			playGame();
		}
		public static void loadGame() {
			//Not implemented
			System.out.println("Not implemented: Returning to Main Menu.");
			return;
		}
		public static void saveGame() {
			
		}
		private static void playGame() {
			while(true) {
				checkForPuzzle();
				if(!skipFight) {
					//System.out.println("Loop 1");
					if(checkForMobs()) {
						//System.out.println("Loop 2");
						Combat();
						//System.out.println("After Combat Returns");
					}
				}
					//String usrCommand = View.navigationCLI(Player.currPlayerLocation(),Area.get(Player.currPlayerLocation()).getRoomName(), Area.get(Player.currPlayerLocation()).hasVisited(),Area.get(Player.currPlayerLocation()).description);
					//Controller.parseUSRInput(usrCommand);
					skipFight = false;
					newView.setView(1);
					newView.draw();
					Controller.singleStepParse(1);
					Area.get(Player.currPlayerLocation()).visit();
				}
			}
		public static ArrayList<Room> getRoomList(){
			return Area;
		}
		public static int[] getNeighbouringRooms() {
			return Area.get(Player.currPlayerLocation()).getNeighbouringRooms();
		}
		//See about reworking this later, to make room host and generate it's own Neighboring Array.
		public static String[] getNbrRoomName() {
			int[] temp = Area.get(Player.currPlayerLocation()).getNeighbouringRooms();
			String[] nbrName = {Area.get(temp[0]).getRoomName(),Area.get(temp[1]).getRoomName(),Area.get(temp[2]).getRoomName(),Area.get(temp[3]).getRoomName()};
			return nbrName;
		}
		public static void explore() {
			boolean isEmpty = Area.get(Player.currPlayerLocation()).rmInv.isEmpty();
			View.exploreList(isEmpty, Area.get(Player.currPlayerLocation()).rmInv.prntList());
		}
		public static void pickup(String usrSelection) {
			//String usrSelection = View.pickup();
			int count = 0;
			Iterator<Item> iter = Area.get(Player.currPlayerLocation()).rmInv.getArrList().iterator();
			Boolean anything = false;
			while(iter.hasNext()) {
				Item X = iter.next();
				count += 1;
				if(X.getName().toLowerCase().contains(usrSelection.toLowerCase())) {
					Player.pInv().addToInv(X);
					iter.remove();
					System.out.println("Item " + X.getName() + " has been picked up,\nand successfully added to the player inventory");
					anything = true;
				}
			}
			if(anything == false) {
				System.out.println("There is no item named " + usrSelection + "in this Room.");
			}
		}
		public static void checkStats() {
			
		}
		public static void showHelp() {
			
		}
		public static void viewMap() {
			
		}
		public static void dropItem(String usrSelection) {
			usrSelection = usrSelection.toLowerCase();
			int count = 0;
			Iterator<Item> iter = Player.playInv.getArrList().iterator();
			Boolean anything = false;
			while(iter.hasNext()) {
				Item X = iter.next();
				count += 1;
				if(X.getName().toLowerCase().contains(usrSelection.toLowerCase())) {
					Area.get(Player.currPlayerLocation()).rmInv.addToInv(X);
					iter.remove();
					System.out.println("Item " + X.getName() + " has been dropped,\nand successfully placed in the: " + Area.get(Player.currPlayerLocation()).name);
					anything = true;
				}
			}
			if(anything == false) {
				System.out.println("There is no item named " + usrSelection + "in your Inventory.");
			}
		}
		public static void checkForPuzzle() {
			Iterator<Puzzle> pIter = Puzzles.iterator();
			while(pIter.hasNext()) {
				Puzzle X = pIter.next();
				if(X.getroomNum() == Player.currPlayerLocation() && X.getCompletion() == false) {
					playPuzzle(X);
				}
			}
		}
		public static void playPuzzle(Puzzle X) {
			System.out.println(X.getQuestion());
			Boolean corrAns = false;
			int attempts = X.getAttempts();
			while(corrAns == false && attempts != 0) {
			String Answer = View.puzzle();
			if(Answer.toLowerCase().contains(X.getAnswer().toLowerCase())) {
				X.markComplete();
				corrAns = true;
				System.out.println("You have correctly solved the puzzle. \n You may proceed.");
				return;
			}
			else {
				attempts -= 1;
				System.out.println("The answer you have provided is wrong, you still have " + attempts + " attempts. Try again.");
			}
			}
			System.out.println("You have failed to solve the puzzle,\ncome back later.");
			Player.updatePlayerLocation(Player.currPlayerLocation() - 1);
		}
		public static Boolean checkForMobs() {
			Boolean mobPresent = !Area.get(Player.currPlayerLocation()).Mobs.isEmpty();
			System.out.println("Is the array empty?" + !mobPresent);
			return mobPresent;
		}
		public static void Combat() {
			ArrayList<Monster> MobsList = Area.get(Player.currPlayerLocation()).Mobs;
			fight = new Combat(MobsList);
			//boolean fightOver = false;
			//Made this global for now.
			System.out.println("List of Mobs");
			newView.setView(4);
			newView.draw();
			Controller.singleStepParse(3);
			Area.get(Player.currPlayerLocation()).visit();
			newView.setView(5);
			while(!fightOver) {
				//System.out.println("List of Mobs");
				newView.draw();
				Controller.singleStepParse(3);
				if(fight.requestStop()) {
					fightOver = true;
					skipFight();
					//System.out.println("After Calling Skip Fight");
					return;
				}
				//System.out.println("End of combat while Loop");
			}
			//System.out.println("Before Combat Return");
		}
		public static Combat getCombatOBJ() {
			return fight;
		}
		public static void skipFight() {
			skipFight = true;
		}
		public static void Fight(Combat S, String target) {
			S.setMobBeingAttacked(target);
			newView.setView(6);
			Boolean isComplete = false;
			while(!isComplete) {
				newView.draw();
				Controller.singleStepParse(4);
				S.damage();
				if(S.getMobHP() <= 0) {
					newView.setView(7);
					newView.draw();
					S.ignore();
					isComplete = true;
					return;
				}
				if(Player.getCurrHP() <= 0) {
					isComplete = true;
					newView.setView(8);
					newView.draw();
					Controller.singleStepParse(0);
				}
			}
		}
	}
