import java.util.concurrent.ThreadLocalRandom;

public class Demo {
    public static void main(String[] args) {
        GameManager gameManager = new GameManager();
        CharacterFactory factory = new CharacterFactory();

        System.out.println("Creating two random heroes to fight!");
        Character c1 = factory.createCharacter();
        Character c2 = factory.createCharacter();

        gameManager.fight(c1, c2);
    }
}

abstract class Character {
    protected int power;
    protected int hp;
    private String name = this.getClass().getSimpleName();

    public abstract void kick(Character c);

    public boolean isAlive() {
        return hp > 0;
    }

    // Getters
    public int getPower() { return power; }
    public int getHp() { return hp; }
    public String getName() { return name; }

    // Setters
    public void setPower(int power) {
        this.power = Math.max(0, power);
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, hp);
    }

    @Override
    public String toString() {
        return name + "{" + "power=" + power + ", hp=" + hp + '}';
    }
}

class Hobbit extends Character {
    public Hobbit() {
        this.hp = 3;
        this.power = 0;
    }

    @Override
    public void kick(Character c) {
        System.out.println("-> " + this.getName() + " is crying...");
    }
}

class Elf extends Character {
    public Elf() {
        this.hp = 10;
        this.power = 10;
    }

    @Override
    public void kick(Character c) {
        if (c.getPower() < this.power) {
            System.out.println("-> " + this.getName() + " uses ultimate power and kills " + c.getName());
            c.setHp(0);
        } else {
            System.out.println("-> " + this.getName() + " weakens " + c.getName());
            c.setPower(c.getPower() - 1);
        }
    }
}

abstract class RandomDamageCharacter extends Character {
    public RandomDamageCharacter(int minPower, int maxPower, int minHp, int maxHp) {
        this.power = ThreadLocalRandom.current().nextInt(minPower, maxPower + 1);
        this.hp = ThreadLocalRandom.current().nextInt(minHp, maxHp + 1);
    }

    @Override
    public void kick(Character c) {
        int damage = ThreadLocalRandom.current().nextInt(0, this.power + 1);
        System.out.println("-> " + this.getName() + " attacks for " + damage + " damage!");
        c.setHp(c.getHp() - damage);
    }
}

class King extends RandomDamageCharacter {
    public King() {
        super(5, 15, 5, 15);
    }
}

class Knight extends RandomDamageCharacter {
    public Knight() {
        super(2, 12, 2, 12);
    }
}


class CharacterFactory {
    public Character createCharacter() {
        int choice = ThreadLocalRandom.current().nextInt(0, 4);
        switch (choice) {
            case 0: return new Hobbit();
            case 1: return new Elf();
            case 2: return new King();
            case 3: return new Knight();
            default: throw new IllegalStateException("Unexpected random value");
        }
    }
}

class GameManager {
    public void fight(Character c1, Character c2) {
        System.out.println("--- THE FIGHT BEGINS ---");
        System.out.println("Fighter 1: " + c1);
        System.out.println("Fighter 2: " + c2);
        System.out.println("------------------------\n");

        while (c1.isAlive() && c2.isAlive()) {
            System.out.println(c1.getName() + " attacks " + c2.getName() + "!");
            c1.kick(c2);
            System.out.println("Status: " + c1 + " | " + c2 + "\n");

            if (!c2.isAlive()) break;

            System.out.println(c2.getName() + " attacks " + c1.getName() + "!");
            c2.kick(c1);
            System.out.println("Status: " + c1 + " | " + c2 + "\n");
        }

        System.out.println("--- FIGHT OVER ---");
        if (c1.isAlive()) {
            System.out.println("The winner is " + c1.getName() + "!");
        } else if (c2.isAlive()) {
            System.out.println("The winner is " + c2.getName() + "!");
        } else {
            System.out.println("It's a draw! Both heroes have fallen.");
        }
    }
}