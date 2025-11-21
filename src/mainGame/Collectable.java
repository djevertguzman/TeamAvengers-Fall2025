//Tuan Nguyen
package mainGame;

import java.util.ArrayList;
public class Collectable extends Item {

    private String collSet;

    public Collectable(int id, String name, String desc, String setName) {
        super(id, name, "Collectible", desc, 0, 0, 0, false);
        this.collSet = setName;
    }

    public boolean checkSetCompl(ArrayList<Item> invList) {
        int count = 0;
        for (Item i : invList) {
            if (i instanceof Collectable) {
                Collectable c = (Collectable) i;
                if (c.collSet.equals(this.collSet)) {
                    count++;
                }
            }
        }
        // Doll Set requires 3 pieces: Head, Torso, Limbs
        return count >= 3;
    }
}

