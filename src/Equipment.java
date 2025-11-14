public class Equipment extends Item {

    private int itmArmor;

    public Equipment(int itmID, String itmName, String itmType, String itmDescription,
                     int augDmg, int augHP, int itmHP, boolean isConsumable,
                     int itmArmor) {

        super(itmID, itmName, itmType, itmDescription, augDmg, augHP, itmHP, isConsumable);
        this.itmArmor = itmArmor;
    }

    public void equip() {
    }

    public void unequip() {

    }
}

