// Skylor Prince
package mainGame;

public class Map {


    private static final String BASE_MAP = """
           ===== Ashfall Manor – Exterior Grounds =====

                               [RM23] Weeping Woods
                                      |
                               [RM22] Back Gate to the Woods
                                      |
               [RM20] Old Storage Shed -- [RM19] Overgrown Backyard -- [RM21] Greenhouse
                                           |
                                         [RM11] Sunroom
                                           |
               [RM3] Fog-Covered Garden -- [RM2] Empty Driveway -- [RM4] Fountain Plaza
                                           |
                                   [RM1] Front Gate Courtyard

           ===== Ashfall Manor – Ground Floor =====

                                      [RM11] Sunroom
                                           |
               [RM3] Fog-Covered Garden -- [RM2] Empty Driveway -- [RM4] Fountain Plaza
                                           |
                                     [RM5] Main Entrance Hall
                                      /       |        \\\\
                               [RM6] Library |      [RM8] Stairwell
                                              |
                                          [RM12] Dining Room
                                              |
                                          [RM13] Kitchen
                                              |
                                      [RM7] First Floor Bathroom

           ===== Ashfall Manor – Upper Floor =====

                               [RM18] Balcony
                                    |
                               [RM16] Servant’s Quarters
                                    |
           [RM14] Second Floor Bathroom -- [RM15] Study Room -- [RM17] Children Playroom
                                    |
                                 [RM9] Gallery Hallway -- [RM10] Master Bedroom
                                    |
                                 [RM8] Stairwell (from Entrance Hall)

           Legend:
           RM#   = Room ID (see Room Table for full descriptions)
           <RM#> = Your current location
           RM#*  = This room contains at least one item
           """;

    // roomIDArr, roomNameArr
    private String[] roomIDArr;
    private String[] roomNameArr;

    public Map() {
        this.roomIDArr = new String[0];
        this.roomNameArr = new String[0];
    }


    @Override
    public String toString() {
        return BASE_MAP;
    }

    //This method is used when the player types “map” during the game.
    //It receives the player’s current room ID, the full list of rooms, and the set of rooms that contain items.
   // It returns the full ASCII map of Ashfall Manor with special markings added.
    //The current room appears with angle brackets, for example <RM5>.
    //Rooms that have items appear with a star inside the brackets, for example [RM5*].
    //The method uses this information to highlight the player’s location and show which rooms contain items without naming the items.
   // The final result is a readable updated map showing the entire layout and visual cues for where the player is and where items exist.

    public String render(String currentRoomID,
                         java.util.Map<String, Room> rooms,
                         java.util.Set<String> roomsWithItems) {

        String mapView = BASE_MAP;

        if (rooms == null) {
            return mapView;
        }

        for (String roomID : rooms.keySet()) {
            String rawTag = "[" + roomID + "]";

            if (!mapView.contains(rawTag)) {
                // Some rooms might not appear on the ASCII map yet
                continue;
            }

            boolean isCurrent = roomID.equalsIgnoreCase(currentRoomID);
            boolean hasItems = roomsWithItems != null && roomsWithItems.contains(roomID);

            String replacement = rawTag;

            // Mark rooms that have items: [RM#*]
            if (hasItems) {
                replacement = "[" + roomID + "*]";
            }

            // Mark current room: <RM#> or <RM#*>
            if (isCurrent) {
                String core = roomID;
                if (hasItems) {
                    core = roomID + "*";
                }
                replacement = "<" + core + ">";
            }

            mapView = mapView.replace(rawTag, replacement);
        }

        return mapView;
    }


     // store room IDs / names from the rooms map.

    public void loadFromRooms(java.util.Map<String, Room> rooms) {
        if (rooms == null || rooms.isEmpty()) {
            roomIDArr = new String[0];
            roomNameArr = new String[0];
            return;
        }

        roomIDArr = new String[rooms.size()];
        roomNameArr = new String[rooms.size()];

        int i = 0;
        for (Room r : rooms.values()) {
            roomIDArr[i] = r.getRoomID();
            roomNameArr[i] = r.getRoomName();
            i++;
        }
    }

    public String[] getRoomIDArr() {
        return roomIDArr;
    }

    public String[] getRoomNameArr() {
        return roomNameArr;
    }
}
