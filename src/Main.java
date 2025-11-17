import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private Map<String, Room> rooms = new HashMap<>();
    private Map<String, Monster> monsters = new HashMap<>();
    private Map<String, Item> artifacts = new HashMap<>();

    private java.util.Random rng = new java.util.Random();

    // --------------------------
    // MAIN METHOD
    // --------------------------
    public static void main(String[] args) {

        Main m = new Main();

        // Load data
        m.loadRooms("Rooms.txt");
        m.loadPuzzles("Puzzle.txt");
        m.loadMonsters("Monsters.txt");
        m.loadArtifacts("Artifacts.txt");

        // Create player and start in RM1
        Player player = new Player("Player");
        Room currentRoom = m.rooms.get("RM1");
        m.spawnMonstersForRoom(currentRoom);

        if (currentRoom == null) {
            System.out.println("ERROR: Starting room RM1 not found!");
            return;
        }

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

        // Give player all 3 doll parts (for testing)
        Item dollHead  = m.artifacts.get("IT14");
        Item dollTorso = m.artifacts.get("IT15");
        Item dollLimbs = m.artifacts.get("IT16");

        if (dollHead != null)  player.addItem(dollHead);
        if (dollTorso != null) player.addItem(dollTorso);
        if (dollLimbs != null) player.addItem(dollLimbs);


        player.setCurrentRoom(currentRoom);
        Scanner in = new Scanner(System.in);

        System.out.println("=====================================");
        System.out.println("   Welcome to Ashfall Manor");
        System.out.println("=====================================");
        System.out.println("Type HELP for commands.\n");

        boolean running = true;

        while (running) {

            // Print room info
            System.out.println("\n== " + currentRoom.getRoomName() + " ==");
            System.out.println(currentRoom.getRoomDesc());

            // Show exits
            String[] exits = currentRoom.getAllExits();

            if (exits != null && exits.length > 0) {
                System.out.println("Exits:");

                for (String e : exits) {

                    // e looks like "N:RM5" or "S:RM2(l)"
                    String[] parts = e.split(":");
                    String dir = parts[0].trim();
                    String dest = parts[1].trim();

                    boolean isLocked = false;

                    // Detect locked door marker "(l)"
                    if (dest.contains("(")) {
                        isLocked = true;
                        dest = dest.substring(0, dest.indexOf("("));  // Remove "(l)" for room lookup
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
                System.out.println("There are no exits here.");
            }

            // Input
            System.out.print("\n> ");
            String input = in.nextLine().trim();

            // Quit
            if (input.equalsIgnoreCase("quit") || input.equalsIgnoreCase("exit")) {
                running = false;
                System.out.println("Goodbye!");
                continue;
            }

            // Help
            if (input.equalsIgnoreCase("help")) {
                player.showHelp();
                continue;
            }

            // PICKUP ITEM
            if (input.toLowerCase().startsWith("pickup")) {

                String[] parts = input.split(" ", 2);

                if (parts.length < 2) {
                    System.out.println("Pick up what?");
                } else {
                    player.pickupItem(parts[1].trim());
                }

                continue;
            }

            // EQUIP ITEM
            if (input.toLowerCase().startsWith("equip")) {

                String[] parts = input.split(" ", 2);

                if (parts.length < 2) {
                    System.out.println("Equip what?");
                } else {
                    player.equipItem(parts[1].trim());
                }

                continue;
            }

            // UNEQUIP ITEM
            if (input.toLowerCase().startsWith("unequip")) {

                String[] parts = input.split(" ", 2);

                if (parts.length < 2) {
                    System.out.println("Unequip what?");
                } else {
                    player.unequipItem(parts[1].trim());
                }

                continue;
            }


            // DROP ITEM
            if (input.toLowerCase().startsWith("drop")) {

                String[] parts = input.split(" ", 2);

                if (parts.length < 2) {
                    System.out.println("Drop what?");
                } else {
                    player.dropItem(parts[1].trim());
                }

                continue;
            }


            // Inventory Command
            if (input.equalsIgnoreCase("inventory") ||
                    input.equalsIgnoreCase("inv")) {

                player.showInventory();
                continue;
            }


            // INSPECT ITEM
            if (input.toLowerCase().startsWith("inspect")) {

                String[] parts = input.split(" ", 2);
                if (parts.length < 2) {
                    System.out.println("Inspect what?");
                } else {
                    player.inspectItem(parts[1].trim());
                }
                continue;
            }


            // USE ITEM
            if (input.toLowerCase().startsWith("use")) {

                String[] parts = input.split(" ", 2);

                if (parts.length < 2) {
                    System.out.println("Use what?");
                } else {
                    player.useItem(parts[1].trim(), m.artifacts);
                    currentRoom = player.getCurrentRoom();
                }

                continue;
            }


            // Look
            if (input.equalsIgnoreCase("look") || input.equalsIgnoreCase("explore")) {
                player.lookAround();
                continue;
            }

            // Check Stats
            if (input.equalsIgnoreCase("check stats") ||
                    input.equalsIgnoreCase("stats") ||
                    input.equalsIgnoreCase("check")) {

                player.showStats();
                continue;
            }

            // -------------------------------------------------------
            // INTERACT (start puzzle logic)
            if (input.equalsIgnoreCase("interact")) {
                player.interactWithPuzzle(in);

                // ---------------------------------------------------------
                // NEW: Check if the puzzle gave a reward
                // ---------------------------------------------------------
                String rewardId = player.collectPendingPuzzleReward();

                if (rewardId != null) {

                    Item rewardItem = null;

                    // FIRST: Try interpreting rewardId as the direct item ID (e.g., "IT16")
                    rewardItem = m.artifacts.get(rewardId);

                    // SECOND: If not found, search by item name (e.g., "Doll Limbs")
                    if (rewardItem == null) {
                        for (Item it : m.artifacts.values()) {
                            if (it.getName().equalsIgnoreCase(rewardId)) {
                                rewardItem = it;
                                break;
                            }
                        }
                    }

                    if (rewardItem != null) {

                        // Check inventory limit (15)
                        if (player.getInventory().size() < 15) {
                            player.addItem(rewardItem);
                            System.out.println("You received: " + rewardItem.getName());
                        } else {
                            // Put item in room instead
                            currentRoom.getRoomInventory().addItem(rewardItem);
                            System.out.println("Your inventory is full. "
                                    + rewardItem.getName() + " was placed in the room.");
                        }
                    } else {
                        System.out.println("WARNING: Reward item '" + rewardId + "' not found in Artifacts.txt");
                    }
                }

                continue;
            }

            // -------------------------------------------------------
            // ENGAGE – start combat with monsters in this room
            // -------------------------------------------------------
            if (input.equalsIgnoreCase("engage")) {

                if (!currentRoom.hasActiveMonsters()) {
                    System.out.println("There are no monsters here to engage.");
                    continue;
                }

                // Start a combat sequence (turn-based)
                m.startCombat(in, player, currentRoom);

                // After combat ends, either player is dead or monsters are gone.
                // If player survived, loop continues.
                continue;
            }


            // Movement
            String dir = parseDirection(input);
            if (dir != null) {
                String destId = currentRoom.getExit(dir);

                if (destId == null) {
                    System.out.println("You can't go that way.");
                } else {
                    Room next = m.rooms.get(destId);
                    if (next != null) {
                        currentRoom = next;
                        player.setCurrentRoom(next);

                        // Spawn monsters for the new room
                        m.spawnMonstersForRoom(currentRoom);

                        System.out.println("You move " + dir + "...");
                    } else {
                        System.out.println("Room " + destId + " is missing.");
                    }
                }
                continue;
            }


            System.out.println("Unknown command. Type HELP.");
        }

        in.close();
    }

    // --------------------------
    // LOADER METHODS BELOW
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

                            String rawDest = e[1].trim();           // e.g. "RM5(l)"
                            boolean locked = rawDest.contains("(l)");

                            // Strip "(l)" from destination
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
            System.out.println("Rooms loaded.");

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
                String roomList = parts[1].trim();       // e.g. "RM2,RM4,RM5"
                String name = parts[2].trim();
                String desc = parts[3].trim();
                int hp = Integer.parseInt(parts[4].trim());
                int dmg = Integer.parseInt(parts[5].trim());
                String drop = parts[6].trim();
                String type = parts[7].trim();           // "Boss", "Random Encounter 50%", "Final Boss"

                // NOTE: Monster currently doesn't store desc properly, but we still pass it via roomId slot
                // If you later add a real description field, you can adjust the constructor.
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
            System.out.println("Monsters loaded.");
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
            System.out.println("Puzzles loaded.");
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
                String rawId = parts[0].trim().toUpperCase();   // "IT1"
                String name = parts[1].trim();
                String type = parts[2].trim();
                String desc = parts[3].trim();
                String effect = parts[4].trim();        // "+25 DMG", "+50 HP", "Armor +20"

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
            System.out.println("Artifacts loaded.");
            System.out.println("Loaded Artifacts:");
            for (String key : artifacts.keySet()) {
                System.out.println("[" + key + "]");
            }

        } catch (Exception e) {
            System.out.println("ERROR loading artifacts: " + e.getMessage());
        }
    }

    private void spawnMonstersForRoom(Room room) {

        if (room == null) return;

        // Clear any previous pack in this room
        room.clearActiveMonsters();

        // Use the room's monster pool (templates)
        java.util.List<Monster> pool = room.getMonsters();
        if (pool == null || pool.isEmpty()) return;

        java.util.List<Monster> spawned = new java.util.ArrayList<>();

        for (Monster template : pool) {

            String type = template.getType();

            // ---- Random Encounter 50% ----
            if (type.toLowerCase().contains("random encounter")) {

                // Extract % from the string (e.g. "Random Encounter 50%")
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
                    int count = 1 + rng.nextInt(3); // 1, 2, or 3

                    for (int i = 0; i < count; i++) {
                        spawned.add(new Monster(template));
                    }
                }
            }
            // ---- Boss or Final Boss (spawn once if not defeated) ----
            else if (type.equalsIgnoreCase("Boss") || type.equalsIgnoreCase("Final Boss")) {

                if (!template.isDefeated()) {
                    spawned.add(new Monster(template));
                }
            }
        }

        // Store spawned pack in the room
        room.setActiveMonsters(spawned);

        if (!spawned.isEmpty()) {
            // Simple feedback for debugging; you can style this however you like
            System.out.println("\nYou sense danger nearby...");
            System.out.println("There are " + spawned.size() + " monster(s) lurking in this area.");
            System.out.println("Type 'engage' to begin combat.");
        }
    }

    // -------------------------------------------------------
    // Handle monster drops after combat
    // -------------------------------------------------------
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

            // Case 1: guaranteed multi-drops, e.g. "Mansion Key / Security Vest"
            if (dropSpec.contains("/")) {
                String[] parts = dropSpec.split("/");
                for (String part : parts) {
                    String itemName = part.trim();
                    giveItemToPlayerOrRoom(itemName, player, room);
                }
            }
            // Case 2: fractional drops, e.g. "⅕ Syringe  ⅓ Bandage"
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
            // Case 3: plain single item name
            else {
                giveItemToPlayerOrRoom(dropSpec, player, room);
            }
        }
    }

    // Helper: resolve item by ID or name, then give to player or room floor
    private void giveItemToPlayerOrRoom(String rewardIdOrName,
                                        Player player,
                                        Room room) {

        if (rewardIdOrName == null || rewardIdOrName.isEmpty()) return;

        Item rewardItem = null;

        // 1) Try as direct ID like "IT10"
        rewardItem = artifacts.get(rewardIdOrName);

        // 2) If not found, search by item name (e.g. "Syringe")
        if (rewardItem == null) {
            for (Item it : artifacts.values()) {
                if (it.getName().equalsIgnoreCase(rewardIdOrName)) {
                    rewardItem = it;
                    break;
                }
            }
        }

        if (rewardItem == null) {
            System.out.println("WARNING: Reward item '" + rewardIdOrName + "' not found in Artifacts.txt");
            return;
        }

        // Try to put in player inventory
        if (player.getInventory().size() < 15) {
            player.addItem(rewardItem);
            System.out.println("You received: " + rewardItem.getName());
        } else {
            // Inventory full -> drop on room floor
            room.getRoomInventory().addItem(rewardItem);
            System.out.println("Your inventory is full. "
                    + rewardItem.getName() + " was placed in the room.");
        }
    }


    // Direction parser
    private static String parseDirection(String input) {
        String cmd = input.trim().toUpperCase();

        return switch (cmd) {
            case "N", "NORTH", "GO N", "GO NORTH" -> "N";
            case "S", "SOUTH", "GO S", "GO SOUTH" -> "S";
            case "E", "EAST", "GO E", "GO EAST" -> "E";
            case "W", "WEST", "GO W", "GO WEST" -> "W";
            default -> null;
        };
    }

    // -------------------------------------------------------
    // Turn-based combat loop
    // -------------------------------------------------------
    private void startCombat(Scanner in, Player player, Room room) {

        java.util.List<Monster> enemies = new java.util.ArrayList<>(room.getActiveMonsters());
        if (enemies.isEmpty()) {
            System.out.println("There are no monsters to fight.");
            return;
        }

        boolean inCombat = true;

        while (inCombat) {

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
            String cmd = in.nextLine().trim();

            // No movement in combat
            String dirTest = parseDirection(cmd);
            if (dirTest != null) {
                System.out.println("You cannot move while in combat!");
                continue;
            }

            // Quit game entirely (optional)
            if (cmd.equalsIgnoreCase("quit") || cmd.equalsIgnoreCase("exit")) {
                System.out.println("You give up your fight...");
                System.exit(0);
            }

            // Help (combat)
            if (cmd.equalsIgnoreCase("help")) {
                System.out.println("""
                        Combat Commands:
                          ATTACK              - attack the first monster
                          USE <itemName>      - use a consumable from inventory
                          STATS / CHECK       - show your stats
                          INVENTORY / INV     - list your items
                          QUIT / EXIT         - quit the game
                        """);
                continue;
            }

            // Stats
            if (cmd.equalsIgnoreCase("stats") ||
                    cmd.equalsIgnoreCase("check") ||
                    cmd.equalsIgnoreCase("check stats")) {

                player.showStats();
                continue;
            }

            // Inventory
            if (cmd.equalsIgnoreCase("inventory") ||
                    cmd.equalsIgnoreCase("inv")) {

                player.showInventory();
                continue;
            }

            // USE item in combat (healing, etc.)
            if (cmd.toLowerCase().startsWith("use")) {
                String[] parts = cmd.split(" ", 2);
                if (parts.length < 2) {
                    System.out.println("Use what?");
                    continue;
                }
                String itemName = parts[1].trim();
                player.useItem(itemName, artifacts);  // uses your existing logic

                // After player action, monsters attack
                monsterAttackTurn(player, enemies);

                if (player.getHp() <= 0) {
                    System.out.println("You have been slain...");
                    inCombat = false;
                }

                continue;
            }

            // ATTACK (simple: always attack the first monster in the list)
            if (cmd.equalsIgnoreCase("attack")) {

                Monster target = enemies.get(0);

                int dmg = player.getAttack();
                if (player.getEquippedWeapon() != null) {
                    dmg += player.getEquippedWeapon().getAugDmg();
                }

                System.out.println("You attack " + target.getName() + " for " + dmg + " damage!");
                target.takeDamage(dmg);

                if (target.getHp() <= 0) {
                    System.out.println("You defeated " + target.getName() + "!");
                    enemies.remove(0);
                }

                // If all enemies dead -> drops + end combat
                if (enemies.isEmpty()) {
                    System.out.println("You defeated all the monsters in this area!");

                    // Boss templates get marked defeated (no respawn)
                    for (Monster template : room.getMonsters()) {
                        if (template.getType().toLowerCase().contains("boss")) {
                            template.setDefeated(true);
                        }
                    }

                    // Drops
                    // Here we pass the dead enemies (we can store them earlier; for now, just reuse room active list)
                    handleMonsterDrops(room.getActiveMonsters(), player, room);

                    room.clearActiveMonsters();
                    inCombat = false;
                    break;
                }

                // Monster turn
                monsterAttackTurn(player, enemies);

                if (player.getHp() <= 0) {
                    System.out.println("You have been slain...");
                    inCombat = false;
                }

                continue;
            }

            System.out.println("Unknown combat command. Type HELP.");
        }
    }

    // Monsters attack the player (Zombie Child can attack twice later if you want)
    private void monsterAttackTurn(Player player, java.util.List<Monster> enemies) {

        for (Monster m : enemies) {
            int dmg = m.getAttack();
            System.out.println(m.getName() + " attacks you for " + dmg + " damage!");
            player.takeDamage(dmg);
        }
    }

}
