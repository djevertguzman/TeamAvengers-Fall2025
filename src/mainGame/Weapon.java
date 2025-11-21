//Tuan Nguyen
package mainGame;

public class Weapon extends Item {

    public Weapon(int id, String name, String desc, int dmg) {
        super(id, name, "Weapon", desc, dmg, 0, 0, false);
    }

    public int dmgWeapon() {
        return this.augDmg;
    }
}

