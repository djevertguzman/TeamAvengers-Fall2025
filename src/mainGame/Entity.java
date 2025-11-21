// Gabriel Peart & Evert Guzman
package mainGame;

public class Entity {

    // These variables are PROTECTED so that classes like Player and Monster
    // that EXTEND this class can use them directly
    protected String name;
    protected int hp; // Health Points how much damage it can take
    protected int attack; // Base damage it deals

    // This is the constructor It runs when we create a new Entity
    public Entity(String name, int hp, int attack) {
        this.name = name;
        this.hp = hp;
        this.attack = attack;
    }

    // --- Getters and Setters Basic data access ---

    public String getName() {
        return name;
    }
    public int getHp() {
        return hp;
    }
    public int getAttack() { return attack; } // Returns the base attack value

    public void setHp(int hp) {
        // We use Mathmax(0 hp) to make sure HP never goes below zero This is important
        this.hp = Math.max(0, hp);
    }
    public void setAttack(int attack) {
        this.attack = attack;
    }

    // --- Common Behavior Actions an Entity can do ---

    // This method handles when the entity loses health
    public void takeDamage(int damage) {
        int reducedDamage = Math.max(0, damage); // Make sure damage isnt negative like healing
        hp -= reducedDamage; // Subtract the damage from HP

        if (hp < 0) hp = 0; // Clamp HP to 0 again just to be safe

        System.out.println(name + " takes " + reducedDamage + " damage Remaining HP: " + hp);
    }

    // A simple check to see if the entity is still alive
    public boolean isAlive() {
        return hp > 0;
    }

}