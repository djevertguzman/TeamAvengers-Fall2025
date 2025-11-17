// Temporary stub so the project compiles while we wire up the new architecture.
public class Game {

    // Controller calls this in one place:
    // Game.pickup(commandSplit[1]);
    public static void pickup(String itemName) {
        // For now, just log that this isn't implemented yet.
        System.out.println("[DEBUG] pickup not implemented yet (tried to pick up: " + itemName + ")");
    }

    // You can add newGame(), saveGame(), loadGame(), etc. here later
    // using your new Player/Room/Item/Monster design.
}
