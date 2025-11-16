
public class Entity {
    protected String name;
    protected int hp;
    protected int attack;

    public Entity(String name, int hp, int attack) {
        this.name = name;
        this.hp = hp;
        this.attack = attack;
    }

    // Getters and setters
    public String getName() {
        return name;
    }
    public int getHp() {
        return hp;
    }
    public int getAttack() { return attack; }

    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
    }
    public void setAttack(int attack) {
        this.attack = attack;
    }

    // Common behavior
    public void takeDamage(int damage) {
        int reducedDamage = Math.max(0, damage);
        hp -= reducedDamage;
        if (hp < 0) hp = 0;
        System.out.println(name + " takes " + reducedDamage + " damage! Remaining HP: " + hp);
    }

    public boolean isAlive() {
        return hp > 0;
    }
}
