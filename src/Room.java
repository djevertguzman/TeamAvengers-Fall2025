import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Room {

    // Active monsters in this room (spawned pack)
    private List<Monster> activeMonsters = new ArrayList<>();  // GABE

    private String roomID;              // Using String (e.g. "RM1") to match the text file
    private String roomName;
    private String roomDesc;

    // direction -> destination roomID (e.g. "N" -> "RM2")
    private HashMap<String, String> rmConnections;

    // Inventory of this room (items that are on the floor, in chests, etc.)
    private Inventory rmInv;

    // Extra associations used by teammates
    private Puzzle puzzle;              // Puzzle in this room (if any)
    private List<Monster> monsters;     // Monster templates that *can* spawn in this room

    // ===== Constructors =====

    public Room(String roomID, String roomName, String roomDesc) {
        this.roomID = roomID;
        this.roomName = roomName;
        this.roomDesc = roomDesc;
        this.rmConnections = new HashMap<>();
        this.rmInv = new Inventory();       // room has its own inventory
        this.monsters = new ArrayList<>();
        this.puzzle = null;
    }

    public String getRoomID() {
        return roomID;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getRoomDesc() {
        return roomDesc;
    }

    public Inventory getRoomInventory() {
        return rmInv;
    }

    public void setRoomInventory(Inventory inventory) {
        this.rmInv = inventory;
    }

    // ===== Active Monster Pack (spawned monsters) =====

    public List<Monster> getActiveMonsters() {  // GABE
        return Collections.unmodifiableList(activeMonsters);
    }

    public void setActiveMonsters(List<Monster> list) { // GABE
        activeMonsters.clear();
        if (list != null) {
            activeMonsters.addAll(list);
        }
    }

    public void clearActiveMonsters() { // GABE
        activeMonsters.clear();
    }

    public boolean hasActiveMonsters() { // GABE
        return !activeMonsters.isEmpty();
    }

    // ===== Connections (rmConnections) =====

    public void addExit(String direction, String destRoomID) {
        if (direction == null || destRoomID == null) return;
        rmConnections.put(direction.toUpperCase(), destRoomID);
    }

    public String getExit(String direction) {
        if (direction == null) return null;
        return rmConnections.get(direction.toUpperCase());
    }

    public String[] getConnections() {
        String[] result = new String[rmConnections.size()];
        int i = 0;
        for (String dir : rmConnections.keySet()) {
            result[i++] = dir + ":" + rmConnections.get(dir);
        }
        return result;
    }

    public java.util.Map<String, String> getConnectionsMap() {
        return Collections.unmodifiableMap(rmConnections);
    }

    // ===== Puzzle association =====

    public Puzzle getPuzzle() {
        return puzzle;
    }

    public void setPuzzle(Puzzle puzzle) {
        this.puzzle = puzzle;
    }

    public boolean hasPuzzle() {
        return puzzle != null;
    }

    // ===== Monster Template Association =====
    // These are NOT the active monsters — these are templates loaded from Monsters.txt

    public void addMonster(Monster monster) {
        if (monster != null) {
            monsters.add(monster);
        }
    }

    public List<Monster> getMonsters() {
        return Collections.unmodifiableList(monsters);
    }

    public boolean hasMonsters() {
        return !monsters.isEmpty();
    }

    // ===== Items in room =====

    public boolean hasItems() {
        return rmInv != null && rmInv.size() > 0;
    }

    @Override
    public String toString() {
        return roomID + " - " + roomName + "\n" + roomDesc;
    }

    public String[] getAllExits() {
        String[] result = new String[rmConnections.size()];
        int i = 0;
        for (String dir : rmConnections.keySet()) {
            result[i++] = dir + ":" + rmConnections.get(dir);
        }
        return result;
    }
}
