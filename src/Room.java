import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


public class Room {


    private String roomID;              // Using String (e.g. "RM1") to match the text file
    private String roomName;
    private String roomDesc;

    // direction -> destination roomID (e.g. "N" -> "RM2")
    private HashMap<String, String> rmConnections;

    // Inventory of this room (items that are on the floor, in chests, etc.)
    private Inventory rmInv;

    // Extra associations used by teammates
    private Puzzle puzzle;              // Puzzle in this room (if any)
    private List<Monster> monsters;     // Monsters that can appear in this room

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

    // ===== Connections (rmConnections) =====


     // Add an exit from this room.
     // Used by Main.loadRooms when it sees "N:RM2,E:RM3" etc.

    public void addExit(String direction, String destRoomID) {
        if (direction == null || destRoomID == null) return;
        rmConnections.put(direction.toUpperCase(), destRoomID);
    }


     //roomID for a direction, e.g. "N", "S", "E", "W".
     // Returns null if there is no exit in that direction.

    public String getExit(String direction) {
        if (direction == null) return null;
        return rmConnections.get(direction.toUpperCase());
    }


     // Returns an array of strings like "N:RM2", "S:RM3", ...

    public String[] getConnections() {
        String[] result = new String[rmConnections.size()];
        int i = 0;
        for (String dir : rmConnections.keySet()) {
            result[i++] = dir + ":" + rmConnections.get(dir);
        }
        return result;
    }


     // Extra helper if Game/Controller wants a read-only view of exits.

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

    // ===== Monster association =====

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

    // ===== Helper for Map: does this room have any items? =====


     // Returns true if this room has at least one item in its inventory.


    public boolean hasItems() {
        // If your Inventory class has a size() or isEmpty() method, use that.
        // For example: return rmInv != null && rmInv.size() > 0;
        // For now, we assume non-null inventory means we CAN have items.
        // You or your teammate can refine this once Inventory is finished.
        return rmInv != null;
    }

    @Override
    public String toString() {
        return roomID + " - " + roomName + "\n" + roomDesc;
    }
}
