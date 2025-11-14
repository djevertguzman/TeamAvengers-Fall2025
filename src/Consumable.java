public class Consumable extends Item {

    private boolean stackable;
    private String consumeGrp;

    public Consumable(int itmID, String itmName, String itmType, String itmDescription,
                      int augDmg, int augHP, int itmHP, boolean isConsumable,
                      boolean stackable, String consumeGrp) {

        super(itmID, itmName, itmType, itmDescription, augDmg, augHP, itmHP, isConsumable);
        this.stackable = stackable;
        this.consumeGrp = consumeGrp;
    }

    public void consume() {
        // Example effect logic
        itmHP = 0;  // or apply augHP, etc.
    }
}

