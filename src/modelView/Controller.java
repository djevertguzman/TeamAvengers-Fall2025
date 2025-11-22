package modelView;

import java.util.Scanner;

import mainGame.Item;
import mainGame.Main;
import mainGame.Player;
import mainGame.Room;

public class Controller {
	private static int context;
	private static Scanner usrInput = new Scanner(System.in);
	//static Room currentRoom = Main.getCurrRm();
	//static Player currPlayer = Main.getPlayerOBJ();
	static Main m = Main.getMainObj();

	public static void getUSRInput() {
		switch (context) {
		case 0:
			navigationSet();
			break;
		case 1:
			combatSet();
			break;
		case 2:
			puzzleSet();
			break;
		case 3:
			break;
		default:
			System.out.println("Error Incorrect View Called");
		}
	}

	private static void navigationSet() {
		Room currentRoom = Main.getCurrRm();
		Player currPlayer  = Main.getPlayerOBJ();
		System.out.println("Navigation Command Set");
		System.out.print("\n> ");
		String currInput = usrInput.nextLine().trim();
		String lower = currInput.toLowerCase();
		String verb;
		String arg;
		String[] parts = lower.split(" ", 2);
		verb = parts[0];
		arg = (parts.length > 1 ? parts[1] : null);
		switch (verb) {
		case "n", "north", "go n", "go north" -> {
			m.playerMovement("N");
			break;
		}
		case "e", "east", "go e", "go east" -> {
			m.playerMovement("E");
			break;
		}
		case "s", "south", "go s", "go south" -> {
			m.playerMovement("S");
			break;
		}
		case "w", "west", "go w", "go west" -> {
			m.playerMovement("W");
			break;
		}
		case "pickup" -> {
			//String[] parts = currInput.split(" ", 2);

			if (parts.length < 2) {
				System.out.println("Pick up what");
			} else {
				Main.getPlayerOBJ().pickupItem(parts[1].trim());
			}

			break;
		}
		case "equip" -> {
			//String[] parts = currInput.split(" ", 2);
			if (parts.length < 2) {
				System.out.println("Equip what");
			} else {
				currPlayer.equipItem(parts[1].trim());
			}

			break;
		}
		case "unequip" -> {
			//String[] parts = currInput.split(" ", 2);

			if (parts.length < 2) {
				System.out.println("Unequip what");
			} else {
				currPlayer.unequipItem(parts[1].trim());
			}

			break;
		}
		case "drop" -> {
			//String[] parts = currInput.split(" ", 2);

			if (parts.length < 2) {
				System.out.println("Drop what");
			} else {
				currPlayer.dropItem(parts[1].trim());
			}

			break;
		}
		case "inventory", "inv" -> {
			currPlayer.showInventory();
			break;
		}
		case "inspect" -> {
			//String[] parts = currInput.split(" ", 2);
			if (parts.length < 2) {
				System.out.println("Inspect what");
			} else {
				currPlayer.inspectItem(parts[1].trim());
			}
			break;
		}
		case "use" -> {
			//String[] parts = currInput.split(" ", 2);

			if (parts.length < 2) {
				System.out.println("Use what");
			} else {
				// We have to pass artifacts so the player class can look up reward items
				currPlayer.useItem(parts[1].trim(), m.getArtifacts());
				currentRoom = currPlayer.getCurrentRoom(); // Update room in case USE moved player
			}

			break;
		}
		case "look", "explore" -> {
			currPlayer.lookAround();
			break;
		}
		case "check stats", "stats", "check" -> {
			currPlayer.showStats();
			break;
		}
		case "interact" -> {
			currPlayer.interactWithPuzzle(usrInput);

			// Check if the puzzle gave a reward
			String rewardId = currPlayer.collectPendingPuzzleReward();

			if (rewardId != null) {

				Item rewardItem = null;

				// FIRST Try interpreting rewardId as the direct item ID (e.g IT16)
				rewardItem = m.getArtifacts().get(rewardId);

				// SECOND If not found search by item name (e.g Doll Limbs)
				if (rewardItem == null) {
					for (Item it : m.getArtifacts().values()) {
						if (it.getName().equalsIgnoreCase(rewardId)) {
							rewardItem = it;
							break;
						}
					}
				}

				if (rewardItem != null) {

					// Check inventory limit (15)
					if (currPlayer.getInventory().size() < 15) {
						currPlayer.addItem(rewardItem);
						System.out.println("You received: " + rewardItem.getName());
					} else {
						// Put item in room instead
						currentRoom.getRoomInventory().addItem(rewardItem);
						System.out
								.println("Your inventory is full " + rewardItem.getName() + " was placed in the room");
					}
				} else {
					System.out.println("WARNING Reward item '" + rewardId + "' not found in Artifactstxt");
				}
			}

			break;
		}
		case "engage" -> {
			if (!currentRoom.hasActiveMonsters()) {
				System.out.println("There are no monsters here to engage");
				break;
			}

			// Start a combat sequence (turn-based)
			m.startCombat(usrInput, currPlayer, currentRoom);

			// After combat ends either player is dead or monsters are gone
			// If player survived loop continues
			break;
		}
		case "exit", "quit" -> {
			// From the main while loop, will have to be implemented externally.
			Main.stopRunning();
			System.out.println("Goodbye");
			break;
		}
		case "help" -> {
			Player.showHelp();
			break;
		}
		case "" -> {
			System.out.println("Unknown command Type HELP");
			break;
		}
		default -> {
			System.out.println("Unknown command Type HELP");
		}
		}
	}
	private static void combatSet() {
		Room currentRoom = Main.getCurrRm();
		Player currPlayer  = Main.getPlayerOBJ();
		System.out.println("Combat Command Set");
		System.out.print("\n> ");
		String currInput = usrInput.nextLine().trim();
		currInput = currInput.toLowerCase();
		switch (currInput) {
		
		
		//This is going to be broken, we are not parsing the rest of the command.
		// This is on the TODO.
		case "use" -> {
			String[] parts = currInput.split(" ", 2);
            if (parts.length < 2) {
                System.out.println("Use what");
                break;
            }
            String itemName = parts[1].trim();
            currPlayer.useItem(itemName, m.getArtifacts());  // uses your existing logic

            // After player action monsters attack
            m.monsterAttackTurn(currPlayer, m.getEnemiesList());

            if (currPlayer.getHp() <= 0) {
                System.out.println("You have been slain");
                m.stopCombatLoop();
            }

            break;
		}
		case "attack" -> {
			Main.startAttack();
		}
		case "inventory","inv" -> {
			currPlayer.showInventory();
            break;
		}
		case "stats","check","check stats" -> {
			currPlayer.showStats();
            break;
		}
		case "help" ->{
			System.out.println("""
                    Combat Commands:
                      ATTACK              - attack the first monster
                      USE <itemName>      - use a consumable from inventory
                      STATS / CHECK       - show your stats
                      INVENTORY / INV     - list your items
                      QUIT / EXIT         - quit the game
                    """);
			break;
		}
		//This is to catch all possible navigation commands.
		case "n", "north", "go n", "go north", "s", "south", "go s", "go south","e", "east", "go e", "go east","w", "west", "go w", "go west" -> {
			System.out.println("You cannot move while in combat");
			break;
		}
		case "exit", "quit" -> {
			// From the main while loop, will have to be implemented externally.
			Main.stopRunning();
			System.out.println("Goodbye");
			break;
		}
		case "" -> {
			System.out.println("Unknown combat command Type HELP");
			break;
		}
		default -> {
			System.out.println("Unknown combat command Type HELP");
			break;
		}
		}
	}
	public static void puzzleSet() {
		
	}
	public int getContext() {
		return context;
	}

	public static void switchContext(int context) {
		Controller.context = context;
	}
}
