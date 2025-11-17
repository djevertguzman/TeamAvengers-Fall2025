//Evert Guzman
import java.util.Scanner;

public class Controller {
	private static Scanner usrkey = new Scanner(System.in);
	private static int ContextSet;
	public static void switchControllerContext(int x) {
		ContextSet = x;
	}
	public static void parseUSRInput() {
		String usrString = usrkey.nextLine();
		switch(ContextSet) {
		case 0:
			MainMenuSet(usrString);
		case 1:
			NavigationSet(usrString);
		case 3:
			CombatSet(usrString);
		case 4:
			CombatFightSet(usrString);
		case 5:
			PuzzleSet(usrString);
		}
	}
	private static void MainMenuSet(String usrInput) {
		switch(usrInput) {
		case "start":
		    //Game.startGame();
			System.out.println("Main Menu Start Option");
		    return;
		case "load":
			//Game.loadGame();
			System.out.println("Main Menu Load Option");
			return;
		case "help":
			System.out.println("Main Menu Help Option");
			System.out.println("Not implemented: Returning to Main Menu.");
		    return;
		case "exit":
			System.out.println("Main Menu exit option");
			System.out.println("See you next time, Goodbye.");
			System.exit(0);
		case "":
			System.out.println("No option selected: Try Again");
			return;
		default:
			System.out.println("Invalid Option: Try Again");
			return;
		}
	}
	private static void NavigationSet(String usrInput) {
		//int nbrRoom[] = Game.getNeighbouringRooms();
		usrInput = usrInput.toLowerCase();
		String commandSplit[] = usrInput.split(" ");
		switch(commandSplit[0]){
		case "n":
			// verify that the chosen option is valid
			if(true) {
				System.out.println("Navigation N Option");
				//Player.updatePlayerLocation(nbrRoom[0]);
			}
			else {
				System.out.println("There is nowhere to go, select another option.");
			}
			break;
			
		case "e":
			if(true) {
				System.out.println("Navigation E Option");
				//Player.updatePlayerLocation(nbrRoom[1]);
				//System.out.println("Branch 2");
			}
			else {
				System.out.println("There is nowhere to go, select another option.");
			}
			break;
		case "s":
			if(true) {
				System.out.println("Navigation S Option");
				//Player.updatePlayerLocation(nbrRoom[2]);
				//System.out.println("Branch 3");
			}else {
				System.out.println("There is nowhere to go, select another option.");
			}
			break;
		case "w":
			if(true) {
				System.out.println("Navigation W Option");
				//Player.updatePlayerLocation(nbrRoom[3]);
				//System.out.println("Branch 3");
			}else {
				System.out.println("There is nowhere to go, select another option.");
			}
			break;
		case "look":
			System.out.println("Navigation Look Option");
			//Game.look();
			break;
		case "pickup":
			System.out.println("Navigation Pickup Option");
			if(commandSplit.length == 1) {
				System.out.println("Command argument required: Try Again");
			}else {
				Game.pickup(commandSplit[1]);
			}
			break;
		case "inventory":
			System.out.println("Navigation Inventory Option");
			//Player.playerInv();
			break;
		case "inspect":
			System.out.println("Navigation Inspect Option");
			if(commandSplit.length == 1) {
				System.out.println("Command argument required: Try Again");
			}else {
				//Player.itemInspect(commandSplit[1]);
			}
			break;
		case "drop":
			System.out.println("Navigation Drop Option");
			if(commandSplit.length == 1) {
				System.out.println("Command argument required: Try Again");
			}else {
				//Game.dropItem(commandSplit[1]);
			}
			break;
		case "equip":
			System.out.println("Navigation Equip Option");
			if(commandSplit.length == 1) {
				System.out.println("Command argument required: Try Again");
			}else {
				//Player.equipItem(commandSplit[1]);
			}
			break;
		case "use":
			System.out.println("Navigation Use Option");
			if(commandSplit.length == 1) {
				System.out.println("Command argument required: Try Again");
			}else {
				//Player.equipItem(commandSplit[1]);
			}
			break;
		case "consume":
			System.out.println("Navigation Consume Option");
			if(commandSplit.length == 1) {
				System.out.println("Command argument required: Try Again");
			}else {
				//Player.equipItem(commandSplit[1]);
			}
			break;
		case "unequip":
			System.out.println("Navigation Unequip Option");
			//Player.unequipItem();
			break;
		case "stats":
			System.out.println("Navigation Stats Option");
			//Player.checkequip();
			break;
		case "map":
			System.out.println("Navigation Map Option");
			//Player.checkHP();
			break;
		case "save":
			System.out.println("Navigation Save Option");
			break;
		case "help":
			System.out.println("Navigation Help Option");
			//View.displayHelp();
			break;
		case "exit":
			System.out.println("See you next time, Goodbye.");
			System.exit(0);
		case "":
			System.out.println("Make sure to select an option.");
			break;
		default:
			System.out.println("There is nowhere to go, select another option.");
			break;
		}
	}
	private static void CombatSet(String usrInput) {
		usrInput = usrInput.toLowerCase();
		String commandSplit[] = usrInput.split(" ");
		switch(commandSplit[0]){
		case "attack":
			System.out.println("Combat 'Attack' Start Option");
			if(commandSplit.length == 1) {
				System.out.println("Command argument required: Try Again");
			}else {
				//Player.equipItem(commandSplit[1]);
			}
			break;
		case "":
			System.out.println("Make sure to select an option.");
			break;
		default:
			System.out.println("There is nowhere to go, select another option.");
			break;
		}
	}
	private static void CombatFightSet(String usrInput) {
		usrInput = usrInput.toLowerCase();
		String commandSplit[] = usrInput.split(" ");
		switch(commandSplit[0]){
		case "attack":
			System.out.println("Combat 'Attack' Argument Option");
			if(commandSplit.length == 1) {
				System.out.println("Command argument required: Try Again");
			}else {
				//Player.equipItem(commandSplit[1]);
			}
			break;
		case "":
			System.out.println("Make sure to select an option.");
			break;
		default:
			System.out.println("There is nowhere to go, select another option.");
			break;
		}
	}
	private static void PuzzleSet(String usrInput) {
		usrInput = usrInput.toLowerCase();
		String commandSplit[] = usrInput.split(" ");
		switch(commandSplit[0]){
		case "hint":
			System.out.println("Puzzle 'hint' Option");
			break;
		case "":
			System.out.println("Make sure to select an option.");
			break;
		default:
			//Heres where we'll be handling the user answer.
			//This may need modification due to the arbitrary nature of this 
			//function.
			//System.out.println("There is nowhere to go, select another option.");
			break;
		}
	}
}
