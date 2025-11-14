public class Collectable extends Item {

    private String collSet;

    public Collectable(int itmID, String itmName, String itmType, String itmDescription,
                       int augDmg, int augHP, int itmHP, boolean isConsumable,
                       String collSet) {

        super(itmID, itmName, itmType, itmDescription, augDmg, augHP, itmHP, isConsumable);
        this.collSet = collSet;
    }

    public boolean checkSetCompl() {
        // Stub: depends on collection logic
        return false;
    }
}
