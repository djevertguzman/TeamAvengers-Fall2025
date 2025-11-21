// Gabriel Peart
package mainGame;

public class Monster extends Entity {

    // --- Monster Specific Fields ---

    private String id; // A unique code for this monster template
    private String description; // Actually holds the room IDs where this monster can spawn
    private String dropItem; // The item ID this monster might drop
    private String type; // Is it a Boss or a Random encounter
    private boolean defeated = false; // Is this monster dead yet

    // --- Constructors The setup methods ---

    // This is the main constructor It sets up the Monster template
    public Monster(String id,
                   String roomList, // This is confusingly named but I wont change it
                   String name,
                   int hp,
                   int attack,
                   String dropItem,
                   String type) {

        super(name, hp, attack); // Calls the Entity constructor for name HP and attack
        this.id = id;
        this.description = roomList;   // roomList is stored here intentionally
        this.dropItem = dropItem;
        this.type = type;
        this.defeated = false; // Fresh monsters are always alive
    }

    // Copy constructor used to create separate instances for packs
    // This is useful so every monster in a group has its own HP bar
    public Monster(Monster other) {
        super(other.getName(), other.getHp(), other.getAttack()); // Copy base stats
        this.id = other.id;
        this.description = other.description;
        this.dropItem = other.dropItem;
        this.type = other.type;
        this.defeated = false; // spawned copies are always alive
    }

    // --- Getters and Setters Accessing the data ---

    public String getId() {
        return id;
    }

    // This is a weird getter because it returns the room list not a description
    public String getDescription() {
        return description;
    }

    public String getDropItem() {
        return dropItem;
    }

    public String getType() {
        return type;
    }

    public boolean isDefeated() {
        return defeated;
    }

    // Set the status to defeated or not
    public void setDefeated(boolean defeated) {
        this.defeated = defeated;
    }

    // toString is a neat method that lets us print the object nicely
    @Override
    public String toString() {
        // I like this format It shows the name HP and damage
        return getName() + " (" + getHp() + " HP, " + getAttack() + " Dmg)";
    }
}