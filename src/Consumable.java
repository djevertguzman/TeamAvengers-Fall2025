

public class Consumable extends Item {

    private boolean stackable;
    private String consumeGroup;   // "Heal", "Buff", etc.

    public Consumable(int id, String name, String desc, int healAmount,
                      boolean stackable, String consumeGroup) {

        super(id, name, "Consumable", desc, 0, 0, healAmount, true);
        this.stackable = stackable;
        this.consumeGroup = consumeGroup;
    }

    public int consume() {

         return itmHP;// healing or effect amount
    }

    public boolean isStackable() {
        return stackable;
    }

    public String getConsumeGroup() {
        return consumeGroup;
    }
}


