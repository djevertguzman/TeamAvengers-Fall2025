import java.util.Scanner;

public class Main {
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
                String id = parts[0].trim();
                String name = parts[1].trim();
                String type = parts[2].trim();
                String desc = parts[3].trim();
                String effectLine = parts[4].trim();

                Item item;

                if (type.equalsIgnoreCase("Weapon")) {
                    int dmg = Integer.parseInt(effectLine.replace("+", "").replace("DMG", "").trim());
                    item = new Weapon(id, name, desc, dmg);
                }
                else if (type.equalsIgnoreCase("Consumable")) {
                    int hp = Integer.parseInt(effectLine.replace("+", "").replace("HP", "").trim());
                    item = new Consumable(id, name, desc, hp);
                }
                else if (type.equalsIgnoreCase("Equipment")) {
                    String[] stats = effectLine.split("\\s+");
                    int armor = Integer.parseInt(stats[1]);
                    int hp = (stats.length > 3) ? Integer.parseInt(stats[3]) : 0;
                    item = new Equipment(id, name, desc, armor, hp);
                }
                else { // Key Item or Collectible
                    item = new KeyItem(id, name, desc, effectLine);
                }

                artifacts.put(id, item);
            }

            file.close();
        } catch (Exception e) {
            System.out.println("Error loading artifacts: " + e.getMessage());
        }
    }

}
