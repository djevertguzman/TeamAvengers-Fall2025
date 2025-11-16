import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private Map<String, Room> rooms = new HashMap<>();
    private Map<String, Monster> monsters = new HashMap<>();
    private Map<String, Item> artifacts = new HashMap<>();

    public static void main(String[] args) {
    	System.out.println("Game Start!");
		View.switchView(0);
		Controller.switchControllerContext(0);
		View.draw();
		Controller.parseUSRInput();
		//This Top Part is Specific to My Code
        Main m = new Main();
        m.loadRooms("Rooms.txt");
        m.loadPuzzles("Puzzles.txt");
        m.loadMonsters("Monsters.txt");
        m.loadArtifacts("Artifacts.txt");

        System.out.println("Loading completed!");
    }



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

                // Load exits only if present
                if (parts.length >= 4) {
                    String[] exits = parts[3].split(",");
                    for (String exit : exits) {
                        String[] keyVal = exit.split(":");
                        String direction = keyVal[0].trim();
                        String dest = keyVal[1].trim().replace("(l)", ""); // remove locked flag if needed
                        room.addExit(direction, dest);
                    }
                }

                rooms.put(id, room);
            }

            file.close();
        } catch (Exception e) {
            System.out.println("Error loading rooms: " + e.getMessage());
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
                String roomID = parts[2].trim();
                String description = parts[3].trim();
                String answer = parts[4].trim();
                String reward = parts[5].trim();
                String penalty = parts[6].trim().replace("Lose", "").replace("HP", "").trim();
                int penaltyHP = Integer.parseInt(penalty);

                Puzzle puzzle = new Puzzle(id, name, description, answer, reward, penaltyHP);

                // Attach puzzle to room
                if (rooms.containsKey(roomID)) {
                    rooms.get(roomID).setPuzzle(puzzle);
                }

            }

            file.close();
        } catch (Exception e) {
            System.out.println("Error loading puzzles: " + e.getMessage());
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
                String roomList = parts[1].trim();
                String name = parts[2].trim();
                String desc = parts[3].trim();
                int hp = Integer.parseInt(parts[4].trim());
                int dmg = Integer.parseInt(parts[5].trim());
                String drop = parts[6].trim();
                String encounterType = parts[7].trim();

                Monster monster = new Monster(id, name, desc, hp, dmg, drop, encounterType);

                // Add monster to every listed room
                String[] roomIDs = roomList.split(",");
                for (String r : roomIDs) {
                    r = r.trim();
                    if (rooms.containsKey(r)) {
                        rooms.get(r).addMonster(monster);
                    }
                }

                monsters.put(id, monster);
            }

            file.close();
        } catch (Exception e) {
            System.out.println("Error loading monsters: " + e.getMessage());
        }
    }
    public void loadArtifacts(String filename) {
        try {
            Scanner file = new Scanner(new File(filename));

            while (file.hasNextLine()) {
                String line = file.nextLine().trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\|");

                String idStr = parts[0].trim();   // ALWAYS STRING FIRST
                String name = parts[1].trim();
                String type = parts[2].trim();
                String desc = parts[3].trim();
                String effectLine = parts[4].trim();

                Item item = null;

                // Convert ID to integer FOR classes that need it
                int idNum = Integer.parseInt(idStr.replaceAll("\\D", ""));

                // ===========================================
                // WEAPON
                // Signature: Weapon(String id, String name, String desc, int dmg)
                // ===========================================
                if (type.equalsIgnoreCase("Weapon")) {

                    int dmg = Integer.parseInt(
                            effectLine.replace("+", "")
                                    .replace("DMG", "")
                                    .trim()
                    );

                    item = new Weapon(idStr, name, desc, dmg);
                }

                // ===========================================
                // CONSUMABLE
                // Signature: Consumable(int, String, String, int, boolean, String)
                // ===========================================
                else if (type.equalsIgnoreCase("Consumable")) {

                    int healAmount = Integer.parseInt(
                            effectLine.replace("+", "")
                                    .replace("HP", "")
                                    .trim()
                    );

                    // Default values since txt file doesn't give stackable/group
                    boolean stackable = false;
                    String consumeGroup = "Heal";

                    item = new Consumable(idNum, name, desc, healAmount, stackable, consumeGroup);
                }

                // ===========================================
                // EQUIPMENT
                // Signature: Equipment(int, String, String, int armor, int hpBonus)
                // ===========================================
                else if (type.equalsIgnoreCase("Equipment")) {

                    int armor = 0;
                    int hpBonus = 0;

                    String[] tokens = effectLine.split("\\s+");

                    for (int i = 0; i < tokens.length; i++) {
                        if (tokens[i].equalsIgnoreCase("Armor")) {
                            armor = Integer.parseInt(tokens[i - 1].replace("+", ""));
                        }
                        else if (tokens[i].equalsIgnoreCase("HP")) {
                            hpBonus = Integer.parseInt(tokens[i - 1].replace("+", ""));
                        }
                    }

                    item = new Equipment(idNum, name, desc, armor, hpBonus);
                }

                // ===========================================
                // COLLECTABLE
                // Signature: Collectable(int, String, String, String setName)
                // effectLine = setName
                // ===========================================
                else if (type.equalsIgnoreCase("Collectible") ||
                        type.equalsIgnoreCase("Key Item")) {

                    item = new Collectable(idNum, name, desc, effectLine);
                }

                // Place into artifacts map (STRING KEY)
                artifacts.put(idStr, item);
            }

            file.close();

        } catch (Exception e) {
            System.out.println("Error loading artifacts: " + e.getMessage());
        }
    }
}
