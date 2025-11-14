
public class Weapon {
}
public class Weapon extends Item {

    public Weapon(int itmID, String itmName, String itmType, String itmDescription,
                  int augDmg, int augHP, int itmHP, boolean isConsumable) {

        super(itmID, itmName, itmType, itmDescription, augDmg, augHP, itmHP, isConsumable);
    }

    public void dmgWeapon(int dmg) {
        dmgItem(dmg);
    }
}
