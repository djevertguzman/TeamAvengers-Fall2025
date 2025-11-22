// Gabriel Peart & Evert Guzman
package mainGame;

import java.util.ArrayList;
import java.util.Scanner;

import modelView.View;

// The Player class extends Entity so it gets base HP and Attack stats
public class Player extends Entity {

    // --- Player Specific Fields ---
    private int armor;
    private Item equippedWeapon;
    private Item equippedArmor;
    private Room currentRoom; // Where the player is right now
    private ArrayList<Item> inventory; // A list to hold all the loot
    private String pendingPuzzleReward = null; // A temporary spot to hold a reward ID

    // This is the constructor It runs when we make a new Player
    public Player(String name) {
        super(name, 100, 25); // Calls the parent Entity constructor Base HP 100 Base Attack 25
        this.armor = 5; // A little bit of base protection
        this.inventory = new ArrayList<>(); // Gotta start with an empty bag
    }

    // -----------------------------
    // LOCATION & MOVEMENT METHODS
    // These handle where the player is in the world
    // -----------------------------

    // Setter for the current Room
    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }
    // Getter for the current Room
    public Room getCurrentRoom() {
        return currentRoom;
    }

    // The LOOK command This is a HUGE method that shows the player everything
    public void lookAround() {
        if (currentRoom == null) {
            System.out.println("You see nothing");
            return;
        }

        //System.out.println("You look around carefully");
        View.setMessage("You look around carefully");

        // Show room name & description again
        //System.out.println("\n== " + currentRoom.getRoomName() + " ==");
        //System.out.println(currentRoom.getRoomDesc());
        View.setMessage("\n== " + currentRoom.getRoomName() + " ==");
        View.setMessage(currentRoom.getRoomDesc());

        // Check if there are items to pick up
        if (currentRoom.hasItems()) {
            //System.out.println("You see some items in this room:");
        	View.setMessage("You see some items in this room:");
            for (Item it : currentRoom.getRoomInventory().getAllItems()) {
                //System.out.println(" - " + it.getName());
            	View.setMessage(" - " + it.getName());
            }
        }

        // Check for puzzles or monsters
        if (currentRoom.getPuzzle() != null) {

            // Puzzle not completed
            if (!currentRoom.getPuzzle().isCompleted()) {
                //System.out.println("There is a puzzle in this room");
                //System.out.println("Type 'interact' to begin solving it");
            	View.setMessage("There is a puzzle in this room");
                View.setMessage("Type 'interact' to begin solving it");
            }
            // Puzzle already completed
            else {
                //System.out.println("The puzzle in this room has already been completed");
            	View.setMessage("The puzzle in this room has already been completed");

                // If the room has active monsters (spawned pack)
                if (currentRoom.hasActiveMonsters()) {
                    java.util.List<Monster> pack = currentRoom.getActiveMonsters();

                    if (!pack.isEmpty()) {
                        String monsterName = pack.get(0).getName(); // same type in pack
                        //System.out.println("You see " + pack.size() + " " + monsterName + "(s) lurking here");
                        //System.out.println("Type 'engage' to begin combat");
                        View.setMessage("You see " + pack.size() + " " + monsterName + "(s) lurking here");
                        View.setMessage("Type 'engage' to begin combat");
                    }
                }
            }
        }
    }

    // -----------------------------
    // STATS & DAMAGE CALCULATION
    // Base stats plus equipment bonuses
    // -----------------------------

    // Getter for the base armor value
    public int getArmor() {
        return armor;
    }
    // Setter for the base armor value
    public void setArmor(int armor) {
        this.armor = armor;
    }

    // Calculates the total damage the player can do
    public int getTotalAttack() {
        int total = getAttack();   // from Entity (base attack)
        if (equippedWeapon != null) {
            total += equippedWeapon.getAugDmg(); // Add the weapons damage bonus
        }
        return total;
    }

    // Calculates the players maximum HP
    public int getTotalMaxHP() {
        int total = getHp();  // from Entity (base HP)
        if (equippedArmor != null) {
            total += equippedArmor.getItemHp(); // Add the armors HP bonus
        }
        return total;
    }

    // This overrides the parents takeDamage method to include armor reduction
    @Override
    public void takeDamage(int damage) {
        int reduced = Math.max(0, damage - armor); // Armor reduces damage but minimum damage taken is 0
        hp -= reduced;
        if (hp < 0) hp = 0; // Cant go below zero HP
        //System.out.println(name + " takes " + reduced + " damage Remaining HP: " + hp);
        View.setMessage(name + " takes " + reduced + " damage Remaining HP: " + hp);
    }

    // Displays the players current stats and equipped gear
    public void showStats() {
        //System.out.println("\n--- Player Stats ---");
        //System.out.println("HP: " + getHp() + " / " + getTotalMaxHP());
        //System.out.println("Armor: " + getArmor());
        //System.out.println("Attack: " + getTotalAttack());
    	View.setMessage("\n--- Player Stats ---");
    	View.setMessage("HP: " + getHp() + " / " + getTotalMaxHP());
    	View.setMessage("Armor: " + getArmor());
    	View.setMessage("Attack: " + getTotalAttack());

        // Equipped Weapon
        if (equippedWeapon != null) {
            Item w = equippedWeapon;
            /*System.out.println("Weapon: " + w.getName() +
                    " (+" + w.getAugDmg() + " DMG)");*/
            View.setMessage("Weapon: " + w.getName() +
                    " (+" + w.getAugDmg() + " DMG)");
        } else {
            //System.out.println("Weapon: None Equipped");
        	View.setMessage("Weapon: None Equipped");
        }

        // Equipped Armor
        if (equippedArmor != null) {
            Item a = equippedArmor;
            /*System.out.println("Equipment: " + a.getName() +
                    " (+" + a.getItemHp() + " HP)");*/
            View.setMessage("Equipment: " + a.getName() +
                    " (+" + a.getItemHp() + " HP)");
        } else {
            //System.out.println("Equipment: None Equipped");
        	View.setMessage("Equipment: None Equipped");
        }
    }


    // -----------------------------
    // INVENTORY MANAGEMENT
    // Adding removing and showing items in the bag
    // -----------------------------

    // Simple add to inventory list
    public void addItem(Item i) {
        inventory.add(i);
    }
    // Simple remove from inventory list
    public void removeItem(Item i) {
        inventory.remove(i);
    }
    // Returns the entire inventory list
    public ArrayList<Item> getInventory()
    {
        return inventory;
    }

    // Loops through the inventory to find an item by name ignoring case
    public Item findItemByName(String name) {
        for (Item i : inventory) {
            if (i.getName().equalsIgnoreCase(name)) return i;
        }
        return null;
    }

    // Lists all items currently in the players inventory
    public void showInventory() {
        if (inventory.isEmpty()) {
            //System.out.println("You are not carrying any items");
        	View.setMessage("You are not carrying any items");
            return;
        }

        //System.out.println("\n--- Inventory ---");
        View.setMessage("\n--- Inventory ---");
        for (Item it : inventory) {
            //System.out.println(" - " + it.getName() + " (" + it.getType() + ")");
            View.setMessage(" - " + it.getName() + " (" + it.getType() + ")");
        }
    }

    // INSPECT ITEM
    public void inspectItem(String itemName) {
        if (itemName == null || itemName.isEmpty()) {
        	View.setMessage("Please specify an item to inspect");
            return;
        }

        Item i = findItemByName(itemName);

        if (i == null) {
        	View.setMessage("That item is not in your inventory");
            return;
        }

        View.setMessage("\n--- Item Details ---");
        View.setMessage("Name: " + i.getName());
        View.setMessage("Type: " + i.getType());
        View.setMessage("Description: " + i.getDescription());

        // Optional show stats only if item has them
        if (i.getAugDmg() > 0) {
        	View.setMessage("Damage Bonus: +" + i.getAugDmg());
        }
        if (i.getAugHP() > 0) {
        	View.setMessage("Heal Amount: +" + i.getAugHP() + " HP");
        }
        if (i.getItemHp() > 0) {
        	View.setMessage("Armor/HP Bonus: +" + i.getItemHp());
        }
    }

    // PICK UP ITEM FROM ROOM
    public void pickupItem(String itemName) {

        if (itemName == null || itemName.isEmpty()) {
        	View.setMessage("Pick up what");
            return;
        }

        if (currentRoom == null) {
        	View.setMessage("You are not in a room");
            return;
        }

        // Room inventory
        Inventory roomInv = currentRoom.getRoomInventory();

        // Find item in the room
        Item item = roomInv.getItem(itemName);

        if (item == null) {
        	View.setMessage("That item is not here");
            return;
        }

        // Check inventory limit
        if (inventory.size() >= 15) {
        	View.setMessage("Your inventory is full");
            return;
        }

        // Move item from room -> player
        roomInv.deleteItem(itemName);
        inventory.add(item);

        View.setMessage("You picked up: " + item.getName());
    }

    // DROP ITEM INTO ROOM
    public void dropItem(String itemName) {

        if (itemName == null || itemName.isEmpty()) {
        	View.setMessage("Drop what");
            return;
        }

        if (currentRoom == null) {
        	View.setMessage("You are not in a room");
            return;
        }

        // Try to find item in player inventory
        Item i = findItemByName(itemName);

        if (i == null) {
        	View.setMessage("You dont have that item");
            return;
        }

        // If item is equipped prevent accidental drop
        if (i == equippedWeapon) {
        	View.setMessage("You cannot drop your currently equipped weapon");
            return;
        }

        if (i == equippedArmor) {
        	View.setMessage("You cannot drop your currently equipped armor");
            return;
        }

        // Remove from player and add to room inventory
        inventory.remove(i);
        currentRoom.getRoomInventory().addItem(i);

        View.setMessage("You dropped: " + i.getName());
    }

    // -----------------------------
    // EQUIPMENT MANAGEMENT
    // Equipping unequipping gear
    // -----------------------------

    // Getter for the equipped weapon
    public Item getEquippedWeapon() {
        return equippedWeapon;
    }
    // Getter for the equipped armor
    public Item getEquippedArmor() {
        return equippedArmor;
    }

    // Equips a specific weapon Item
    public void equipWeapon(Item weapon) {
        if (weapon != null && weapon.getType().equalsIgnoreCase("Weapon")) {
            this.equippedWeapon = weapon;
            View.setMessage("You equipped: " + weapon.getName());
        }
    }

    // Equips a specific armor Item
    public void equipArmor(Item armorItem) {
        if (armorItem != null && armorItem.getType().equalsIgnoreCase("Equipment")) {
            this.equippedArmor = armorItem;
            View.setMessage("You equipped: " + armorItem.getName());
        }
    }

    // General Equip method that figures out the type itself
    public void equipItem(String itemName) {

        if (itemName == null || itemName.isEmpty()) {
        	View.setMessage("Equip what");
            return;
        }

        // Find item in inventory
        Item item = findItemByName(itemName);

        if (item == null) {
        	View.setMessage("You dont have that item");
            return;
        }

        // Determine if item is a weapon (has damage stat)
        boolean isWeapon = item.getAugDmg() > 0;

        // Determine if item is armor/equipment (has HP or armor stat)
        boolean isArmor = item.getItemHp() > 0;

        // EQUIP AS WEAPON
        if (isWeapon) {
            this.equippedWeapon = item;
            View.setMessage("You equipped: " + item.getName());
            return;
        }

        // EQUIP AS ARMOR
        if (isArmor) {
            this.equippedArmor = item;
            View.setMessage("You equipped: " + item.getName());
            return;
        }

        // If item has no stats that qualify it
        View.setMessage("This item cannot be equipped");
    }

    // UNEQUIP ITEM
    public void unequipItem(String itemName) {

        if (itemName == null || itemName.isEmpty()) {
        	View.setMessage("Unequip what");
            return;
        }

        // Normalize name for comparison
        String name = itemName.trim().toLowerCase();

        // Check if this is the equipped weapon
        if (equippedWeapon != null && equippedWeapon.getName().toLowerCase().equals(name)) {
        	View.setMessage("You unequipped: " + equippedWeapon.getName());
            equippedWeapon = null;
            return;
        }

        // Check if this is the equipped armor
        if (equippedArmor != null && equippedArmor.getName().toLowerCase().equals(name)) {
        	View.setMessage("You unequipped: " + equippedArmor.getName());
            equippedArmor = null;
            return;
        }

        // If item is in inventory but not equipped
        Item invItem = findItemByName(itemName);
        if (invItem != null) {
        	View.setMessage("That item is not currently equipped");
        } else {
        	View.setMessage("You dont have that item");
        }
    }

    // -----------------------------
    // PUZZLE INTERACTION
    // Methods for solving puzzles and handling rewards
    // -----------------------------

    // Getter for the pending puzzle reward ID
    public String collectPendingPuzzleReward() {
        String temp = pendingPuzzleReward;
        pendingPuzzleReward = null; // Clear it out after collection
        return temp;
    }


    // INTERACT start puzzle logic
    public void interactWithPuzzle(Scanner in) {

        // Get the puzzle in the players current room
    	View.switchView(3);
        Puzzle puzzle = currentRoom.getPuzzle();

        // No puzzle
        if (puzzle == null) {
        	View.setMessage("There is no puzzle in this room");
            return;
        }

        // Already solved
        if (puzzle.isCompleted()) {
        	View.setMessage("The puzzle in this room has already been completed");
            return;
        }

        // Start puzzle
        View.setMessage("\nPuzzle: " + puzzle.getPuzName());
        View.setMessage(puzzle.getQuestion());

        View.setMessage("Your answer: ");
        View.draw();
        String userAns = in.nextLine().trim();

        boolean correct = puzzle.userAnswer(userAns);

        if (correct) {
        	View.setMessage("You solved the puzzle");

            // -----------------------------------------
            // NEW CODE Dispense Puzzle Reward
            // -----------------------------------------
            String rewardId = puzzle.getRewardItemIdOrName();

            if (rewardId != null && !rewardId.isEmpty()) {

                // Store reward to a temporary variable for Main to handle later
                this.pendingPuzzleReward = rewardId;

                View.setMessage("Reward available: " + rewardId);
            }

            return;
        }

        // Incorrect answer
        int dmg = puzzle.getPenaltyHP();
        this.takeDamage(dmg);

        View.setMessage("Incorrect You lose " + dmg + " HP");
        View.setMessage("Remaining HP: " + this.getHp());

        // Puzzle locks if attempts exceeded (future use)
        if (puzzle.isCompleted() && puzzle.getPuzAttempt() >= puzzle.getPuzMaxAtt()) {
        	View.setMessage("The puzzle locks and cannot be retried");
        }

    }

    // -----------------------------
    // ITEM USAGE
    // What happens when the player USES an item
    // -----------------------------

    // USE ITEM (consumables key items doll fusion)
    public void useItem(String itemName, java.util.Map<String, Item> artifacts) {

        if (itemName == null || itemName.isEmpty()) {
        	View.setMessage("Use what");
            return;
        }

        // Find item in inventory
        Item item = findItemByName(itemName);

        if (item == null) {
        	View.setMessage("You dont have that item");
            return;
        }

        String type = item.getType();

        // -------------------------------------------------------
        // 1 CONSUMABLE (heals player)
        // -------------------------------------------------------
        if (type.equalsIgnoreCase("Consumable")) {

            int heal = item.getAugHP();

            if (heal <= 0) {
            	View.setMessage("This item cannot be used");
                return;
            }

            // Heal the player
            this.hp += heal;
            View.setMessage("You consume " + item.getName() + " and restore " + heal + " HP");
            View.setMessage("Current HP: " + this.hp);

            // Remove item after use
            inventory.remove(item);
            return;
        }

        // -------------------------------------------------------
        // 2 KEY ITEM (unlock doors)
        // Description contains Unlock RM
        // -------------------------------------------------------
        if (type.equalsIgnoreCase("Key Item")) {

            String desc = item.getDescription().toUpperCase();

            // Does description contain target room
            if (!desc.contains("RM")) {
            	View.setMessage("This item cannot be used right now");
                return;
            }

            // Extract room ID inside description (like RM5)
            String unlockTarget = "";
            for (String part : desc.split("\\s+")) {
                if (part.startsWith("RM")) {
                    unlockTarget = part.trim();
                    break;
                }
            }

            if (unlockTarget.isEmpty()) {
            	View.setMessage("This item cannot be used right now");
                return;
            }

            boolean unlocked = false;

            // Look through the current rooms exits
            for (String dir : currentRoom.getConnectionsMap().keySet()) {

                // If this exit is actually locked according to RoomlockedExits
                if (currentRoom.isExitLocked(dir)) {

                    String dest = currentRoom.getConnectionsMap().get(dir);

                    // If this locked exit leads to the target room
                    if (dest.equalsIgnoreCase(unlockTarget)) {

                        // UNLOCK IT
                        // Overwrite the exit cleanly (already clean)
                        currentRoom.addExit(dir, dest);

                        // Remove the lock
                        currentRoom.unlockExit(dir);

                        View.setMessage("You used " + item.getName() +
                                " The door to " + dest + " is now unlocked");

                        unlocked = true;
                        break;
                    }
                }
            }

            if (!unlocked) {
            	View.setMessage("This item cannot be used right now");
                return;
            }

            // Consume key after unlocking
            inventory.remove(item);
            return;
        }

        // -------------------------------------------------------
        // 3 DOLL FUSION (Collectible) must be in RM20
        // -------------------------------------------------------
        if (type.equalsIgnoreCase("Collectible")) {

            // Only works in Old Storage Shed (RM20)
            if (!currentRoom.getRoomID().equalsIgnoreCase("RM20")) {

                boolean hasHead  = findItemByName("Doll Head")  != null;
                boolean hasTorso = findItemByName("Doll Torso") != null;
                boolean hasLimbs = findItemByName("Doll Limbs") != null;

                if (hasHead && hasTorso && hasLimbs) {
                	View.setMessage("Faint humming can be heard from Old Storage Shed");
                } else {
                	View.setMessage("This item cannot be used right now");
                }
                return;
            }

            // Check for all doll parts
            Item head  = findItemByName("Doll Head");
            Item torso = findItemByName("Doll Torso");
            Item limbs = findItemByName("Doll Limbs");

            if (head == null || torso == null || limbs == null) {
            	View.setMessage("This cannot be used without the other pieces");
                return;
            }

            // Fuse into Mothers Grief (IT17)
            Item motherGrief = artifacts.get("IT17");

            if (motherGrief == null) {
            	View.setMessage("Something went wrong the fused weapon could not be created");
                return;
            }

            // Remove old pieces
            inventory.remove(head);
            inventory.remove(torso);
            inventory.remove(limbs);

            // Add new weapon
            inventory.add(motherGrief);

            View.setMessage("The doll pieces shudder violently");
            View.setMessage("They merge into a horrific weapon Mothers Grief");

            return;
        }

        // -------------------------------------------------------
        // 4 Not usable
        // -------------------------------------------------------
        View.setMessage("This item cannot be used");
    }

    // -----------------------------
    // HELP COMMAND
    // Prints a list of all game commands
    // -----------------------------
    public static void showHelp() {
    	View.setMessage("""
            Commands:
              N / NORTH / GO N / GO NORTH
              S / SOUTH / GO S / GO SOUTH
              E / EAST  / GO E / GO EAST
              W / WEST  / GO W / GO WEST

              LOOK / EXPLORE   - look around
              INVENTORY / INV  - show inventory
              CHECK STATS      - view player stats
              INTERACT         - start a puzzle
              EQUIP <item>     - equip a weapon or armor
              USE <item>       - use key items or doll parts
              CONSUME <item>   - consume a consumable
              DROP <item>      - drop an item
              PICKUP <item>    - pick up an item

              MAP              - show full map
              SAVE             - save game
              LOAD             - load game

              HELP             - show this list
              QUIT / EXIT      - leave the game
            """);
    }
}