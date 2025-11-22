//Gabriel Peart & Evert Guzman
package mainGame;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import modelView.Controller;

public class Main {

    // --- Game Data Storage ---
    // These maps hold all the templates and room objects loaded from files
    private Map<String, Room> rooms = new HashMap<>(); // All rooms by ID
    private Map<String, Monster> monsters = new HashMap<>(); // All monster templates by ID
    private Map<String, Item> artifacts = new HashMap<>(); // All item templates by ID
    private java.util.Random rng = new java.util.Random(); // A random number generator for luck
    private static boolean running = true; //Controls if the main loop is running.
    static Player player = new Player("Player");
    static Room currentRoom;
    static Main m;
    static Boolean remoteAttackCommand = false;
    static java.util.List<Monster> enemies;
    boolean inCombat;

    // --------------------------
    // MAIN METHOD This is where the program starts
    // --------------------------
    public static void main(String[] args) {

        m = new Main(); // We create an instance of Main to run the non-static methods

        // Load data from files
        m.loadRooms("Rooms.txt");
        m.loadPuzzles("Puzzle.txt");
        m.loadMonsters("Monsters.txt");
        m.loadArtifacts("Artifacts.txt");

        // Create player and start in RM1
        currentRoom = m.rooms.get("RM1"); // Always start here

        if (currentRoom == null) {
            System.out.println("ERROR Starting room RM1 not found");
            return;
        }

        // Give the player basic starting gear and equip it
        Item startingWeapon = m.artifacts.get("IT1");
        Item startingArmor  = m.artifacts.get("IT7");

        if (startingWeapon != null) {
            player.addItem(startingWeapon);
            player.equipWeapon(startingWeapon);   // Auto-equip
        }

        if (startingArmor != null) {
            player.addItem(startingArmor);
            player.equipArmor(startingArmor);     // Auto-equip
        }

        // Set up the game environment
        player.setCurrentRoom(currentRoom);
        m.spawnMonstersForRoom(currentRoom); // Check for monsters in the starting room
        //Scanner in = new Scanner(System.in); // The object used to read player input

        System.out.println("=====================================");
        System.out.println("   Welcome to Ashfall Manor");
        System.out.println("=====================================");
        System.out.println("Type HELP for commands\n");


        // -------------------------------------------------------
        // GAME LOOP This runs until the player quits or dies
        // -------------------------------------------------------
        while (running) {
        	//Scream Test No idea why i set this. 
        	//String playDir = null;
            // Print room info at the start of every turn
        	Controller.switchContext(0);
            System.out.println("\n== " + currentRoom.getRoomName() + " ==");
            System.out.println(currentRoom.getRoomDesc());

            // Show exits This is complicated logic to show locked doors
            String[] exits = currentRoom.getAllExits();

            if (exits != null && exits.length > 0) {
                System.out.println("Exits:");

                for (String e : exits) {

                    // e looks like N:RM5 or S:RM2(l)
                    String[] parts = e.split(":");
                    String dir = parts[0].trim();
                    String dest = parts[1].trim();

                    boolean isLocked = false;

                    // Detect locked door marker (l)
                    if (dest.contains("(")) {
                        isLocked = true;
                        dest = dest.substring(0, dest.indexOf("("));  // Remove (l) for room lookup
                    }

                    // Lookup the real room to display its name
                    Room nextRoom = m.rooms.get(dest);

                    // Build the line for this exit
                    String exitLine = "  [" + dir + "] ";

                    if (nextRoom != null) {
                        exitLine += nextRoom.getRoomName();
                    } else {
                        exitLine += dest;   // fallback (should not happen)
                    }

                    if (isLocked) {
                        exitLine += " (Locked)";
                    }

                    System.out.println(exitLine);
                }

            } else {
                System.out.println("There are no exits here");
            }
            
            Controller.getUSRInput();

        }

    }

    // --------------------------
    // HELPER METHODS
    // --------------------------
    
    //Moving Movement to it's own method. 
    public void playerMovement(String dir) {
    	if (dir != null) {
            String destId = currentRoom.getExit(dir); // Get the room ID for that direction

            if (destId == null) {
                System.out.println("You cant go that way");
            } else {
                Room next = m.rooms.get(destId);
                if (next != null) {
                    currentRoom = next; // Update the current room variable
                    player.setCurrentRoom(next); // Tell the player object they moved

                    // Spawn monsters for the new room
                    m.spawnMonstersForRoom(currentRoom);

                    System.out.println("You move " + dir + "");
                } else {
                    System.out.println("Room " + destId + " is missing");
                }
            }
        }
    }

    // Helper resolve item by ID or name then give to player or room floor
    private void giveItemToPlayerOrRoom(String rewardIdOrName,
                                        Player player,
                                        Room room) {

        if (rewardIdOrName == null || rewardIdOrName.isEmpty()) return;

        Item rewardItem = null;

        // 1 Try as direct ID like IT10
        rewardItem = artifacts.get(rewardIdOrName);

        // 2 If not found search by item name (e.g Syringe)
        if (rewardItem == null) {
            for (Item it : artifacts.values()) {
                if (it.getName().equalsIgnoreCase(rewardIdOrName)) {
                    rewardItem = it;
                    break;
                }
            }
        }

        if (rewardItem == null) {
            System.out.println("WARNING Reward item '" + rewardIdOrName + "' not found in Artifactstxt");
            return;
        }

        // Try to put in player inventory
        if (player.getInventory().size() < 15) {
            player.addItem(rewardItem);
            System.out.println("You received: " + rewardItem.getName());
        } else {
            // Inventory full -> drop on room floor
            room.getRoomInventory().addItem(rewardItem);
            System.out.println("Your inventory is full "
                    + rewardItem.getName() + " was placed in the room");
        }
    }

    // --------------------------
    // MONSTER AND COMBAT LOGIC
    // --------------------------

    private void spawnMonstersForRoom(Room room) {

        if (room == null) return;

        // Clear any previous pack in this room
        room.clearActiveMonsters();

        // Use the rooms monster pool (templates)
        java.util.List<Monster> pool = room.getMonsters();
        if (pool == null || pool.isEmpty()) return;

        java.util.List<Monster> spawned = new java.util.ArrayList<>();

        for (Monster template : pool) {

            String type = template.getType();

            // ---- Random Encounter 50% ----
            if (type.toLowerCase().contains("random encounter")) {

                // Extract % from the string (e.g "Random Encounter 50%")
                int chance = 50;
                java.util.regex.Matcher m = java.util.regex.Pattern
                        .compile("(\\d+)")
                        .matcher(type);
                if (m.find()) {
                    chance = Integer.parseInt(m.group(1));
                }

                int roll = rng.nextInt(100) + 1;
                if (roll <= chance) {
                    // Spawn a pack of 1–3 monsters of this type
                    int count = 1 + rng.nextInt(3); // 1 2 or 3

                    for (int i = 0; i < count; i++) {
                        spawned.add(new Monster(template)); // Use copy constructor
                    }
                }
            }
            // ---- Boss or Final Boss (spawn once if not defeated) ----
            else if (type.equalsIgnoreCase("Boss") || type.equalsIgnoreCase("Final Boss")) {

                if (!template.isDefeated()) {
                    spawned.add(new Monster(template)); // Use copy constructor
                }
            }
        }

        // Store spawned pack in the room
        room.setActiveMonsters(spawned);

        if (!spawned.isEmpty()) {
            System.out.println("\nYou sense danger nearby");
            System.out.println("There are " + spawned.size() + " monster(s) lurking in this area");
            System.out.println("Type 'engage' to begin combat");
        }
    }

    // Turn-based combat loop This is a massive method
    public void startCombat(Scanner in, Player player, Room room) {
        enemies = new java.util.ArrayList<>(room.getActiveMonsters());
        //Setting up remote handling;
        Controller.switchContext(1);
        if (enemies.isEmpty()) {
            System.out.println("There are no monsters to fight");
            return;
        }

        inCombat = true;

        while (inCombat) {
        	Boolean attack = false;
            // Show basic combat status
            System.out.println("\n--- Combat ---");
            System.out.println("Your HP: " + player.getHp());
            System.out.println("Enemies:");
            int idx = 1;
            for (Monster m : enemies) {
                System.out.println("  " + idx + ") " + m.getName()
                        + " (" + m.getHp() + " HP, " + m.getAttack() + " DMG)");
                idx++;
            }

            System.out.print("\nCombat command (attack/use/stats/inventory/help): ");
            //Calling the controller to handle user input.
            Controller.getUSRInput();

            // ATTACK (simple always attack the first monster in the list)
            //Implemented a method to remotely trigger attack.
            // Will probably have issues, more testing will be required.
            if (remoteAttackCommand) {
            	remoteAttackCommand = false;
                Monster target = enemies.get(0);

                int dmg = player.getAttack();
                if (player.getEquippedWeapon() != null) {
                    dmg += player.getEquippedWeapon().getAugDmg();
                }

                System.out.println("You attack " + target.getName() + " for " + dmg + " damage");
                target.takeDamage(dmg);

                if (target.getHp() <= 0) {
                    System.out.println("You defeated " + target.getName() + "");
                    enemies.remove(0);
                }

                // If all enemies dead -> drops + end combat
                if (enemies.isEmpty()) {
                    System.out.println("You defeated all the monsters in this area");

                    // Boss templates get marked defeated (no respawn)
                    for (Monster template : room.getMonsters()) {
                        if (template.getType().toLowerCase().contains("boss")) {
                            template.setDefeated(true);
                        }
                    }

                    // Drops
                    // We pass the list of monsters that were active in the room
                    handleMonsterDrops(room.getActiveMonsters(), player, room);

                    room.clearActiveMonsters(); // Clear the dead ones
                    inCombat = false;
                    break;
                }

                // Monster turn
                monsterAttackTurn(player, enemies);

                if (player.getHp() <= 0) {
                    System.out.println("You have been slain");
                    inCombat = false;
                }

                continue;
            }

        }
    }

    // Monsters attack the player
    public void monsterAttackTurn(Player player, java.util.List<Monster> enemies) {

        for (Monster m : enemies) {
            int dmg = m.getAttack();
            System.out.println(m.getName() + " attacks you for " + dmg + " damage");
            player.takeDamage(dmg);
        }
    }

    // Handle monster drops after combat
    private void handleMonsterDrops(java.util.List<Monster> defeated,
                                    Player player,
                                    Room room) {

        if (defeated == null || defeated.isEmpty()) return;

        for (Monster m : defeated) {

            String dropSpec = m.getDropItem();
            if (dropSpec == null || dropSpec.equalsIgnoreCase("Null") || dropSpec.isEmpty()) {
                continue;
            }

            dropSpec = dropSpec.trim();

            // Case 1 guaranteed multi-drops e.g "Mansion Key / Security Vest"
            if (dropSpec.contains("/")) {
                String[] parts = dropSpec.split("/");
                for (String part : parts) {
                    String itemName = part.trim();
                    giveItemToPlayerOrRoom(itemName, player, room);
                }
            }
            // Case 2 fractional drops e.g "⅕ Syringe  ⅓ Bandage"
            else if (dropSpec.contains("⅕") || dropSpec.contains("⅓")
                    || dropSpec.contains("½") || dropSpec.contains("⅙")) {

                String[] tokens = dropSpec.split("\\s+");
                for (int i = 0; i < tokens.length - 1; i += 2) {
                    String frac = tokens[i];
                    String itemName = tokens[i + 1];

                    int chance = 0;
                    if (frac.equals("⅕")) chance = 20;   // 20%
                    else if (frac.equals("⅓")) chance = 33; // 33%
                    else if (frac.equals("½")) chance = 50; // 50%
                    else if (frac.equals("⅙")) chance = 16; // ~16%

                    if (chance <= 0) continue;

                    int roll = rng.nextInt(100) + 1;
                    if (roll <= chance) {
                        giveItemToPlayerOrRoom(itemName, player, room);
                    }
                }
            }
            // Case 3 plain single item name
            else {
                giveItemToPlayerOrRoom(dropSpec, player, room);
            }
        }
    }

    // --------------------------
    // LOADER METHODS BELOW These read the data files
    // --------------------------

    public void loadRooms(String filename) {
        try {
            Scanner file = new Scanner(new File(filename));

            while (file.hasNextLine()) {
                String line = file.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\|");
                String id = parts[0].trim();
                String name = parts[1].trim();
                String desc = parts[2].trim();

                Room room = new Room(id, name, desc);

                if (parts.length > 3) {
                    String[] exitArr = parts[3].trim().split(",");
                    for (String ex : exitArr) {

                        if (ex.contains(":")) {
                            String[] e = ex.trim().split(":");
                            String dir = e[0].trim().toUpperCase();

                            String rawDest = e[1].trim();           // e.g RM5(l)
                            boolean locked = rawDest.contains("(l)");

                            // Strip (l) from destination
                            String cleanDest = rawDest.replace("(l)", "").trim();

                            // Add normalized exit
                            room.addExit(dir, cleanDest);

                            // If original was locked → record it
                            if (locked) {
                                room.lockExit(dir);
                            }
                        }
                    }
                }

                rooms.put(id, room);
            }

            file.close();
        } catch (Exception e) {
            System.out.println("ERROR loading rooms: " + e.getMessage());
        }
    }


    public void loadMonsters(String filename) {
        try {
            Scanner file = new Scanner(new File(filename));

            while (file.hasNextLine()) {
                String line = file.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\|");

                String id = parts[0].trim();
                String roomList = parts[1].trim();       // e.g RM2RM4RM5
                String name = parts[2].trim();
                String desc = parts[3].trim();
                int hp = Integer.parseInt(parts[4].trim());
                int dmg = Integer.parseInt(parts[5].trim());
                String drop = parts[6].trim();
                String type = parts[7].trim();           // Boss Random Encounter 50% Final Boss

                // NOTE Monster currently doesnt store desc properly but we still pass it via roomId slot
                // If you later add a real description field you can adjust the constructor
                Monster m = new Monster(id, roomList, name, hp, dmg, drop, type);
                monsters.put(id, m);

                // Attach this monster template to each listed room
                String[] roomIDs = roomList.split(",");
                for (String rid : roomIDs) {
                    rid = rid.trim();
                    Room r = rooms.get(rid);
                    if (r != null) {
                        r.addMonster(m);
                    }
                }
            }

            file.close();
        } catch (Exception e) {
            System.out.println("ERROR loading monsters: " + e.getMessage());
        }
    }

    public void loadPuzzles(String filename) {
        try {
            Scanner file = new Scanner(new File(filename));

            while (file.hasNextLine()) {
                String line = file.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\|");

                String id = parts[0].trim();
                String name = parts[1].trim();
                String roomId = parts[2].trim();
                String riddle = parts[3].trim();
                String solution = parts[4].trim();
                String reward = parts[5].trim();
                int hpPenalty = Integer.parseInt(parts[6].replaceAll("[^0-9]", ""));

                Puzzle p = new Puzzle(id, name, riddle, solution, reward, hpPenalty);

                Room r = rooms.get(roomId);
                if (r != null) r.setPuzzle(p);
            }

            file.close();
        } catch (Exception e) {
            System.out.println("ERROR loading puzzles: " + e.getMessage());
        }
    }

    public void loadArtifacts(String filename) {
        try {
            Scanner file = new Scanner(new File(filename));

            while (file.hasNextLine()) {
                String line = file.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\|");

                // Raw data
                String rawId = parts[0].trim().toUpperCase();   // IT1
                String name = parts[1].trim();
                String type = parts[2].trim();
                String desc = parts[3].trim();
                String effect = parts[4].trim();        // +25 DMG +50 HP Armor +20

                // Convert ID from IT# to integer
                int id = Integer.parseInt(rawId.substring(2));  // IT1 -> 1

                // Default stats
                int augDmg = 0;
                int augHP = 0;
                int itmHP = 0;
                boolean consumable = false;

                // Parse effect text
                effect = effect.toUpperCase();

                if (effect.contains("DMG")) {
                    augDmg = Integer.parseInt(effect.replaceAll("[^0-9]", ""));
                }
                if (effect.contains("HP")) {
                    augHP = Integer.parseInt(effect.replaceAll("[^0-9]", ""));
                    consumable = true;
                }
                if (effect.contains("ARMOR")) {
                    itmHP = Integer.parseInt(effect.replaceAll("[^0-9]", ""));
                }

                // Create the item using your 8-parameter constructor
                Item item = new Item(id, name, type, desc, augDmg, augHP, itmHP, consumable);
                artifacts.put(rawId, item);
            }

            file.close();
        } catch (Exception e) {
            System.out.println("ERROR loading artifacts: " + e.getMessage());
        }
    }
    //These blocks are so that we can get the Objects from main.
    //To be able to interact with them remotely.
    public static void stopRunning() {
    	running = false;
    }
    public static Player getPlayerOBJ() {
    	return player;
    }
    public static Room getCurrRm() {
    	return currentRoom;
    }
    public static Main getMainObj() {
    	return m;
    }
    public Map<String, Item> getArtifacts(){
		return artifacts;
    }
    public static void startAttack() {
    	remoteAttackCommand = true;
    }
    public java.util.List<Monster> getEnemiesList(){
    	return enemies;
    }
    public void stopCombatLoop() {
    	inCombat = false;
    }
}