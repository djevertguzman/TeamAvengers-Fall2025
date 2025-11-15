public class Equipment extends Item {

    private int armorValue;

    public Equipment(int id, String name, String desc, int armor, int hpBonus) {
        super(id, name, "Equipment", desc, 0, hpBonus, 0, false);
        this.armorValue = armor;
    }

    public int getArmorValue() {
        return armorValue;
    }

    public int equip() {
        return armorValue;
    }

    public int unequip() {
        return armorValue;
    }
}


