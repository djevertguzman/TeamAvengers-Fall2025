package modelView;

import java.util.Scanner;

import mainGame.Item;
import mainGame.Main;
import mainGame.Player;
import mainGame.Room;

public class Controller {
	private static int context;
	private static Scanner usrInput = new Scanner(System.in);
	static Room currentRoom = Main.getCurrRm();
	static Player currPlayer = Main.getPlayerOBJ();
	static Main m = Main.getMainObj();

	public static void getUSRInput() {
		switch (context) {
		case 0:
			navigationSet();
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		default:
			System.out.println("Error Incorrect View Called");
		}
	}

	private static void navigationSet() {
		System.out.print("\n> ");
		String currInput = usrInput.nextLine().trim();
		currInput = currInput.toLowerCase();
		switch (currInput) {
		case "n", "north", "go n", "go north" -> {
			Main.playerMovement("N");
			break;
		}
		case "e", "east", "go e", "go east" -> {
			Main.playerMovement("E");
			break;
		}
		case "s", "south", "go s", "go south" -> {
			Main.playerMovement("S");
			break;
		}
		case "w", "west", "go w", "go west" -> {
			Main.playerMovement("W");
			break;
		}
		case "pickup" -> {
			String[] parts = currInput.split(" ", 2);

			if (parts.length < 2) {
				System.out.println("Pick up what");
			} else {
				Main.getPlayerOBJ().pickupItem(parts[1].trim());
			}

			break;
		}
		case "equip" -> {
			String[] parts = currInput.split(" ", 2);
			if (parts.length < 2) {
				System.out.println("Equip what");
			} else {
				currPlayer.equipItem(parts[1].trim());
			}

			break;
		}
		case "unequip" -> {
			String[] parts = currInput.split(" ", 2);

			if (parts.length < 2) {
				System.out.println("Unequip what");
			} else {
				currPlayer.unequipItem(parts[1].trim());
			}

			break;
		}
		case "drop" -> {
			String[] parts = currInput.split(" ", 2);

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
			String[] parts = currInput.split(" ", 2);
			if (parts.length < 2) {
				System.out.println("Inspect what");
			} else {
				currPlayer.inspectItem(parts[1].trim());
			}
			break;
		}
		case "use" -> {
			String[] parts = currInput.split(" ", 2);

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
		case "enguage" -> {
			if (!currentRoom.hasActiveMonsters()) {
				System.out.println("There are no monsters here to engage");
				break;
			}

			// Start a combat sequence (turn-based)
			// Null was "in" setting to null correcting another issue.
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

	public int getContext() {
		return context;
	}

	public static void switchContext(int context) {
		Controller.context = context;
	}
}
