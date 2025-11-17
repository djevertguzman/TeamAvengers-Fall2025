public class Monster extends Entity {
    private String id;
    private String description;
    private String dropItem;
    private String type; // "Boss" or "Random"
    private boolean defeated = false;

    // Matches how you call it in Main.loadMonsters:
    // new Monster(id, name, desc, hp, dmg, drop, encounterType);
    public Monster(String id,
                   String roomList,
                   String name,
                   int hp,
                   int attack,
                   String dropItem,
                   String type) {

        super(name, hp, attack);
        this.id = id;
        this.description = roomList;   // roomList is stored here intentionally
        this.dropItem = dropItem;
        this.type = type;
        this.defeated = false;
    }

    // Copy constructor – used to create separate instances for packs
    public Monster(Monster other) {
        super(other.getName(), other.getHp(), other.getAttack());
        this.id = other.id;
        this.description = other.description;
        this.dropItem = other.dropItem;
        this.type = other.type;
        this.defeated = false; // spawned copies are always alive
    }

    public String getId() {
        return id;
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

    public boolean isDefeated() {
        return defeated;
    }

    public void setDefeated(boolean defeated) {
        this.defeated = defeated;
    }

    @Override
    public String toString() {
        return getName() + " (" + getHp() + " HP, " + getAttack() + " Dmg)";
    }
}
