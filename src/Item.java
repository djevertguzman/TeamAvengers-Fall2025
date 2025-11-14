
public class Item {

    private int itmID;
    private String itmName;
    private String itmType;
    private String itmDescription;
    private int augDmg;
    private int augHP;
    private int itmHP;
    private boolean isConsumable;

    // Constructor
    public Item(int itmID, String itmName, String itmType, String itmDescription,
                int augDmg, int augHP, int itmHP, boolean isConsumable) {
        this.itmID = itmID;
        this.itmName = itmName;
        this.itmType = itmType;
        this.itmDescription = itmDescription;
        this.augDmg = augDmg;
        this.augHP = augHP;
        this.itmHP = itmHP;
        this.isConsumable = isConsumable;
    }

    // ---- Getters ----
    public String getName() {
        return itmName;
    }

    public String getDescription() {
        return itmDescription;
    }

    public String getType() {
        return itmType;
    }

    // ---- Methods ----
    // Damages or reduces HP of the item
    public void dmgItem(int dmg) {
        itmHP -= dmg;
        if (itmHP < 0) itmHP = 0;
    }

    @Override
    public String toString() {
        return itmName + " (" + itmType + "): " + itmDescription;
    }
}

