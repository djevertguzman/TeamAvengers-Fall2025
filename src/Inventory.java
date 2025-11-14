import java.util.ArrayList;

public class Inventory {

    private int invID;
    private ArrayList<Item> items;

    public Inventory(int invID) {
        this.invID = invID;
        this.items = new ArrayList<>();
    }

    public void addItem(Item item) {
        items.add(item);
    }

    public Item getItem(int index) {
        if (index < 0 || index >= items.size()) return null;
        return items.get(index);
    }

    public void mvItem(String name) {
        Item found = null;
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                found = item;
                break;
            }
        }
        if (found != null) {
            items.remove(found);
            items.add(found);
        }
    }

    public boolean deleteItem(String name) {
        return items.removeIf(item -> item.getName().equalsIgnoreCase(name));
    }

    public String inspectItem(String name) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(name)) {
                return item.toString();
            }
        }
        return "Item not found.";
    }
}
