import java.util.ArrayList;
import java.util.Scanner;

public class Player extends Entity {
    private int armor;
    private Item equippedWeapon;
    private Item equippedArmor;
    private Room currentRoom;
    private ArrayList<Item> inventory;

    public Player(String name) {
        super(name, 100, 25); // HP, Base Attack from Analysis doc
        this.armor = 5;
        this.inventory = new ArrayList<>();
    }

    // Location
    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }
    public Room getCurrentRoom() {
        return currentRoom;
    }

    // Stats
    public int getArmor() {
        return armor;
    }
    public void setArmor(int armor) {
        this.armor = armor;
    }

    // Equipment
    public void equipWeapon(Item weapon) {
        if (weapon != null && weapon.getType().equalsIgnoreCase("Weapon")) {
            this.equippedWeapon = weapon;
            System.out.println("You equipped: " + weapon.getName());
        }
    }

    public void equipArmor(Item armorItem) {
        if (armorItem != null && armorItem.getType().equalsIgnoreCase("Equipment")) {
            this.equippedArmor = armorItem;
            System.out.println("You equipped: " + armorItem.getName());
        }
    }

    public Item getEquippedWeapon() {
        return equippedWeapon;
    }
    public Item getEquippedArmor() {
        return equippedArmor;
    }

    public int getTotalAttack() {
        int total = getAttack();   // from Entity (25)
        if (equippedWeapon != null) {
            total += equippedWeapon.getAugDmg();
        }
        return total;
    }

    public int getTotalMaxHP() {
        int total = getHp();  // from Entity (base HP)
        if (equippedArmor != null) {
            total += equippedArmor.getItemHp();
        }
        return total;
    }


    // Inventory
    public void showInventory() {
        if (inventory.isEmpty()) {
            System.out.println("You are not carrying any items.");
            return;
        }

        System.out.println("\n--- Inventory ---");
        for (Item it : inventory) {
            System.out.println(" - " + it.getName() + " (" + it.getType() + ")");
        }
    }

    // Puzzle Reward
    private String pendingPuzzleReward = null;

    public String collectPendingPuzzleReward() {
        String temp = pendingPuzzleReward;
        pendingPuzzleReward = null;
        return temp;
    }


    // Look Around
    public void lookAround() {
        if (currentRoom == null) {
            System.out.println("You see nothing...");
            return;
        }

        System.out.println("You look around carefully...");

        // Show room name & description again
        System.out.println("\n== " + currentRoom.getRoomName() + " ==");
        System.out.println(currentRoom.getRoomDesc());

        // If the room has items
        if (currentRoom.hasItems()) {
            System.out.println("You see some items in this room:");
            for (Item it : currentRoom.getRoomInventory().getAllItems()) {
                System.out.println(" - " + it.getName());
            }
        }

        // If the room has a puzzle
        if (currentRoom.getPuzzle() != null) {

            // Puzzle not completed
            if (!currentRoom.getPuzzle().isCompleted()) {
                System.out.println("There is a puzzle in this room.");
                System.out.println("Type 'interact' to begin solving it.");
            }
            // Puzzle already completed
            else {
                System.out.println("The puzzle in this room has already been completed.");
                // If the room has active monsters (spawned pack)

                if (currentRoom.hasActiveMonsters()) {
                    java.util.List<Monster> pack = currentRoom.getActiveMonsters();

                    if (!pack.isEmpty()) {
                        String monsterName = pack.get(0).getName(); // same type in pack
                        System.out.println("You see " + pack.size() + " " + monsterName + "(s) lurking here.");
                        System.out.println("Type 'engage' to begin combat.");
                    }
                }
            }
        }
    }



    // INTERACT (start puzzle logic)
    public void interactWithPuzzle(Scanner in) {

        // Get the puzzle in the player's current room
        Puzzle puzzle = currentRoom.getPuzzle();

        // No puzzle
        if (puzzle == null) {
            System.out.println("There is no puzzle in this room.");
            return;
        }

        // Already solved
        if (puzzle.isCompleted()) {
            System.out.println("The puzzle in this room has already been completed.");
            return;
        }

        // Start puzzle
        System.out.println("\nPuzzle: " + puzzle.getPuzName());
        System.out.println(puzzle.getQuestion());

        System.out.print("Your answer: ");
        String userAns = in.nextLine().trim();

        boolean correct = puzzle.userAnswer(userAns);

        if (correct) {
            System.out.println("You solved the puzzle!");

            // -----------------------------------------
            // NEW CODE: Dispense Puzzle Reward
            // -----------------------------------------
            String rewardId = puzzle.getRewardItemIdOrName();

            if (rewardId != null && !rewardId.isEmpty()) {

                // Look up the real item using artifacts in Main
                // Player cannot access artifacts directly
                // So we will store reward to a temporary list,
                // Main will hand the item to the player later.
                this.pendingPuzzleReward = rewardId;

                System.out.println("Reward available: " + rewardId);
            }

            return;
        }

        // Incorrect answer
        int dmg = puzzle.getPenaltyHP();
        this.takeDamage(dmg);

        System.out.println("Incorrect! You lose " + dmg + " HP.");
        System.out.println("Remaining HP: " + this.getHp());

        // Puzzle locks if attempts exceeded (future use)
        if (puzzle.isCompleted() && puzzle.getPuzAttempt() >= puzzle.getPuzMaxAtt()) {
            System.out.println("The puzzle locks and cannot be retried.");
        }

    }


    // PICK UP ITEM FROM ROOM
    public void pickupItem(String itemName) {

        if (itemName == null || itemName.isEmpty()) {
            System.out.println("Pick up what?");
            return;
        }

        if (currentRoom == null) {
            System.out.println("You are not in a room.");
            return;
        }

        // Room inventory
        Inventory roomInv = currentRoom.getRoomInventory();

        // Find item in the room
        Item item = roomInv.getItem(itemName);

        if (item == null) {
            System.out.println("That item is not here.");
            return;
        }

        // Check inventory limit
        if (inventory.size() >= 15) {
            System.out.println("Your inventory is full.");
            return;
        }

        // Move item from room -> player
        roomInv.deleteItem(itemName);
        inventory.add(item);

        System.out.println("You picked up: " + item.getName());
    }

    // EQUIP ITEM
    public void equipItem(String itemName) {

        if (itemName == null || itemName.isEmpty()) {
            System.out.println("Equip what?");
            return;
        }

        // Find item in inventory
        Item item = findItemByName(itemName);

        if (item == null) {
            System.out.println("You don't have that item.");
            return;
        }

        // Determine if item is a weapon (has damage stat)
        boolean isWeapon = item.getAugDmg() > 0;

        // Determine if item is armor/equipment (has HP or armor stat)
        boolean isArmor = item.getItemHp() > 0;

        // EQUIP AS WEAPON
        if (isWeapon) {
            this.equippedWeapon = item;
            System.out.println("You equipped: " + item.getName());
            return;
        }

        // EQUIP AS ARMOR
        if (isArmor) {
            this.equippedArmor = item;
            System.out.println("You equipped: " + item.getName());
            return;
        }

        // If item has no stats that qualify it
        System.out.println("This item cannot be equipped.");
    }

    // UNEQUIP ITEM
    public void unequipItem(String itemName) {

        if (itemName == null || itemName.isEmpty()) {
            System.out.println("Unequip what?");
            return;
        }

        // Normalize name for comparison
        String name = itemName.trim().toLowerCase();

        // Check if this is the equipped weapon
        if (equippedWeapon != null && equippedWeapon.getName().toLowerCase().equals(name)) {
            System.out.println("You unequipped: " + equippedWeapon.getName());
            equippedWeapon = null;
            return;
        }

        // Check if this is the equipped armor
        if (equippedArmor != null && equippedArmor.getName().toLowerCase().equals(name)) {
            System.out.println("You unequipped: " + equippedArmor.getName());
            equippedArmor = null;
            return;
        }

        // If item is in inventory but not equipped
        Item invItem = findItemByName(itemName);
        if (invItem != null) {
            System.out.println("That item is not currently equipped.");
        } else {
            System.out.println("You don't have that item.");
        }
    }


    // DROP ITEM INTO ROOM
    public void dropItem(String itemName) {

        if (itemName == null || itemName.isEmpty()) {
            System.out.println("Drop what?");
            return;
        }

        if (currentRoom == null) {
            System.out.println("You are not in a room.");
            return;
        }

        // Try to find item in player inventory
        Item i = findItemByName(itemName);

        if (i == null) {
            System.out.println("You don't have that item.");
            return;
        }

        // If item is equipped, prevent accidental unequip OR auto-unequip
        if (i == equippedWeapon) {
            System.out.println("You cannot drop your currently equipped weapon.");
            return;
        }

        if (i == equippedArmor) {
            System.out.println("You cannot drop your currently equipped armor.");
            return;
        }

        // Remove from player and add to room inventory
        inventory.remove(i);
        currentRoom.getRoomInventory().addItem(i);

        System.out.println("You dropped: " + i.getName());
    }



    // Inspect an item in player inventory
    public void inspectItem(String itemName) {
        if (itemName == null || itemName.isEmpty()) {
            System.out.println("Please specify an item to inspect.");
            return;
        }

        Item i = findItemByName(itemName);

        if (i == null) {
            System.out.println("That item is not in your inventory.");
            return;
        }

        System.out.println("\n--- Item Details ---");
        System.out.println("Name: " + i.getName());
        System.out.println("Type: " + i.getType());
        System.out.println("Description: " + i.getDescription());

        // Optional: show stats only if item has them
        if (i.getAugDmg() > 0) {
            System.out.println("Damage Bonus: +" + i.getAugDmg());
        }
        if (i.getAugHP() > 0) {
            System.out.println("Heal Amount: +" + i.getAugHP() + " HP");
        }
        if (i.getItemHp() > 0) {
            System.out.println("Armor/HP Bonus: +" + i.getItemHp());
        }
    }

    // CHECK STATS
    public void showStats() {
        System.out.println("\n--- Player Stats ---");
        System.out.println("HP: " + getHp() + " / " + getTotalMaxHP());
        System.out.println("Armor: " + getArmor());
        System.out.println("Attack: " + getTotalAttack());

        // Equipped Weapon
        if (equippedWeapon != null) {
            Item w = equippedWeapon;
            System.out.println("Weapon: " + w.getName() +
                    " (+" + w.getAugDmg() + " DMG)");
        } else {
            System.out.println("Weapon: None Equipped");
        }

        // Equipped Armor
        if (equippedArmor != null) {
            Item a = equippedArmor;
            System.out.println("Equipment: " + a.getName() +
                    " (+" + a.getItemHp() + " HP)");
        } else {
            System.out.println("Equipment: None Equipped");
        }
    }


    // HELP
    public void showHelp() {
        System.out.println("""
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

    // USE ITEM (consumables, key items, doll fusion)
    public void useItem(String itemName, java.util.Map<String, Item> artifacts) {

        if (itemName == null || itemName.isEmpty()) {
            System.out.println("Use what?");
            return;
        }

        // Find item in inventory
        Item item = findItemByName(itemName);

        if (item == null) {
            System.out.println("You don't have that item.");
            return;
        }

        String type = item.getType();

        // -------------------------------------------------------
        // 1. CONSUMABLE (heals player)
        // -------------------------------------------------------
        if (type.equalsIgnoreCase("Consumable")) {

            int heal = item.getAugHP();

            if (heal <= 0) {
                System.out.println("This item cannot be used.");
                return;
            }

            // Heal the player
            this.hp += heal;
            System.out.println("You consume " + item.getName() + " and restore " + heal + " HP.");
            System.out.println("Current HP: " + this.hp);

            // Remove item after use
            inventory.remove(item);
            return;
        }

        // -------------------------------------------------------
        // 2. KEY ITEM (unlock doors)
        // Description contains: "Unlock RM#"
        // -------------------------------------------------------
        if (type.equalsIgnoreCase("Key Item")) {

            String desc = item.getDescription().toUpperCase();

            if (!desc.contains("UNLOCK")) {
                System.out.println("This item cannot be used right now.");
                return;
            }

            // Extract room ID after the word UNLOCK
            // Example: "Unlock RM5" -> "RM5"
            String unlockTarget = desc.substring(desc.indexOf("UNLOCK") + 6).trim();

            boolean unlocked = false;

            // Loop through all exits in the current room
            for (String dir : currentRoom.getConnectionsMap().keySet()) {

                String rawExit = currentRoom.getConnectionsMap().get(dir);

                // A locked exit looks like: "RM5(l)"
                if (rawExit.contains("(")) {

                    // Extract "RM5" and ignore "(l)"
                    String dest = rawExit.substring(0, rawExit.indexOf("(")).trim();

                    // Check if this is the door the key unlocks
                    if (dest.equalsIgnoreCase(unlockTarget)) {

                        // Overwrite the exit with the clean version (unlocked)
                        currentRoom.addExit(dir, dest);

                        System.out.println("You used " + item.getName() +
                                ". The door to " + dest + " is now unlocked!");

                        unlocked = true;
                        break;
                    }
                }
            }

            if (!unlocked) {
                System.out.println("This item cannot be used right now.");
                return;
            }

            // Key is consumed after unlocking
            inventory.remove(item);
            return;
        }

        // -------------------------------------------------------
        // 3. DOLL FUSION (Collectible) – must be in RM20
        // -------------------------------------------------------
        if (type.equalsIgnoreCase("Collectible")) {

            // Only works in Old Storage Shed (RM20)
            if (!currentRoom.getRoomID().equalsIgnoreCase("RM20")) {

                boolean hasHead  = findItemByName("Doll Head")  != null;
                boolean hasTorso = findItemByName("Doll Torso") != null;
                boolean hasLimbs = findItemByName("Doll Limbs") != null;

                if (hasHead && hasTorso && hasLimbs) {
                    System.out.println("Faint humming can be heard from Old Storage Shed...");
                } else {
                    System.out.println("This item cannot be used right now.");
                }
                return;
            }

            // Check for all doll parts
            Item head  = findItemByName("Doll Head");
            Item torso = findItemByName("Doll Torso");
            Item limbs = findItemByName("Doll Limbs");

            if (head == null || torso == null || limbs == null) {
                System.out.println("This cannot be used without the other pieces.");
                return;
            }

            // Fuse into Mother's Grief (IT17)
            Item motherGrief = artifacts.get("IT17");

            if (motherGrief == null) {
                System.out.println("Something went wrong… the fused weapon could not be created.");
                return;
            }

            // Remove old pieces
            inventory.remove(head);
            inventory.remove(torso);
            inventory.remove(limbs);

            // Add new weapon
            inventory.add(motherGrief);

            System.out.println("The doll pieces shudder violently...");
            System.out.println("They merge into a horrific weapon: Mother's Grief!");

            return;
        }

        // -------------------------------------------------------
        // 4. Not usable
        // -------------------------------------------------------
        System.out.println("This item cannot be used.");
    }



    public void addItem(Item i) {
        inventory.add(i);
    }
    public void removeItem(Item i) {
        inventory.remove(i);
    }
    public ArrayList<Item> getInventory()
    {
        return inventory;
    }
    public Item findItemByName(String name) {
        for (Item i : inventory) {
            if (i.getName().equalsIgnoreCase(name)) return i;
        }
        return null;
    }

    @Override
    public void takeDamage(int damage) {
        int reduced = Math.max(0, damage - armor);
        hp -= reduced;
        if (hp < 0) hp = 0;
        System.out.println(name + " takes " + reduced + " damage! Remaining HP: " + hp);
    }
}
