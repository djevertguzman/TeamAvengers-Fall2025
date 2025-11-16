<<<<<<< HEAD
public class Monster {
=======

public class Monster extends Entity {
    private String id;
    private String roomId;
    private String description;
    private String dropItem;
    private String type; // "Boss" or "Random"

    public Monster(String id, String roomId, String name, int hp, int damage, String dropItem, String type) {
        super(name, hp, damage);
        this.id = id;
        this.roomId = roomId;
        this.description = description;
        this.dropItem = dropItem;
        this.type = type;
    }

    // Getters
    public String getId() {
        return id;
    }
    public String getRoomId() {
        return roomId;
    }
    public String getDescription() {
        return description;
    }
    public String getDropItem() {
        return dropItem;
    }
    public String getType() {
        return type;
    }

    @Override
    public String toString() {

        return name + " (" + hp + " HP, " + attack + " Dmg)";
    }
>>>>>>> Gabriel-Branch
}
