import java.util.List;
import java.util.ArrayList;

public class Player extends Entity {
    private int armor;
    private Item equippedWeapon;
    private Item equippedArmor;
    private Room currentRoom;
    private ArrayList<Item> inventory;

    public Player(String name) {
        super(name, 100, 25); // HP, Base Attack from Analysis doc
        this.armor = 5;
        this.inventory = new ArrayList<>();
    }

    // Location
    public void setCurrentRoom(Room room) {
        this.currentRoom = room;
    }
    public Room getCurrentRoom() {
        return currentRoom;
    }

    // Stats
    public int getArmor() {
        return armor;
    }
    public void setArmor(int armor) {
        this.armor = armor;
    }

    // Equipment
    public void equipWeapon(Item weapon) {
        if (weapon != null && weapon.getType().equalsIgnoreCase("Weapon")) {
            this.equippedWeapon = weapon;
            System.out.println("You equipped: " + weapon.getName());
        }
    }

    public void equipArmor(Item armorItem) {
        if (armorItem != null && armorItem.getType().equalsIgnoreCase("Equipment")) {
            this.equippedArmor = armorItem;
            System.out.println("You equipped: " + armorItem.getName());
        }
    }

    public Item getEquippedWeapon() {
        return equippedWeapon;
    }
    public Item getEquippedArmor() {
        return equippedArmor;
    }

    // Inventory
    public void addItem(Item i) {
        inventory.add(i);
    }
    public void removeItem(Item i) {
        inventory.remove(i);
    }
    public ArrayList<Item> getInventory()
    {
        return inventory;
    }
    public Item findItemByName(String name) {
        for (Item i : inventory) {
            if (i.getName().equalsIgnoreCase(name)) return i;
        }
        return null;
    }

    @Override
    public void takeDamage(int damage) {
        int reduced = Math.max(0, damage - armor);
        hp -= reduced;
        if (hp < 0) hp = 0;
        System.out.println(name + " takes " + reduced + " damage! Remaining HP: " + hp);
    }
}
