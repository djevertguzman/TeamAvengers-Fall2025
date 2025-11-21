// Tuan Nguyen
package mainGame;

import java.util.ArrayList;
public class Inventory {

    private ArrayList<Item> items;
    private final int MAX_SIZE = 15;

    public Inventory() {
        items = new ArrayList<>();
    }

    public boolean addItem(Item item) {
        if (items.size() >= MAX_SIZE) {
            return false;
        }
        items.add(item);
        return true;
    }


    public boolean deleteItem(String itemName) {
        return items.removeIf(i -> i.getName().equalsIgnoreCase(itemName));
    }

    public Item getItem(String itemName) {
        for (Item i : items) {
            if (i.getName().equalsIgnoreCase(itemName)) {
                return i;
            }
        }
        return null;
    }

    public String inspectItem(String itemName) {
        Item i = getItem(itemName);
        if (i == null) return "Item not found.";

        return "\nItem: " + i.getName() +
                "\nType: " + i.getType() +
                "\nDescription: " + i.getDescription() +
                "\n";
    }

    public ArrayList<Item> getAllItems() {
        return items;
    }

    public int size() {
        return items.size();
    }
}
