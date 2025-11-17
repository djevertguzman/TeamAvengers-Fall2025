//Tuan Nguyen
public class Item {

    private int itmID;
    private String itmName;
    private String itmType;
    private String itmDescription;

    // Stats
    int augDmg;
    private int augHP;
    int itmHP;
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

    public String getName() {
        return itmName;
    }
    public String getDescription() {
        return itmDescription;
    }

    public String getType() {
        return itmType;
    }
    public int getAugDmg() {
        return augDmg;
    }
    public int getAugHP() {
        return augHP;
    }
    public int getItemHp() {
        return itmHP;
    }
    public boolean isConsumable() {
        return isConsumable;
    }
}

