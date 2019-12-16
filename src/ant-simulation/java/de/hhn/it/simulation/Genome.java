package de.hhn.it.simulation;

public class Genome {
    private final float speed;
    private final float healthPoints;
    private final int antLimit;
    private final float fighterRatio;

    private final int capacity;
    private final float attackDamage;
    private final float attackSpeed;
    private final float movementSpeed;
    private final float foodConsumption;

    public Genome() {
        this.speed = 1 + (float) Helper.randomDouble(2);
        this.healthPoints = 3 + (float) Helper.randomDouble(7);
        this.antLimit = 50 + Helper.randomInt(50);

        this.capacity = Math.round(healthPoints / 3);
        this.attackDamage = healthPoints / 2;
        this.attackSpeed = 1 / speed;
        this.movementSpeed = speed * 2 / 3;
        this.foodConsumption = (speed * 2 + healthPoints) / 2;
        this.fighterRatio = 5 + (float) Helper.randomDoubleUpperLowerBound(2);
    }

    public Genome(Genome oldGenome) {
        float newSpeed = oldGenome.getSpeed() + (float) Helper.randomDoubleUpperLowerBound(0.1);
        if (newSpeed >= 3) {
            this.speed = 3;
        } else if (newSpeed <= 0.5) {
            this.speed = 0.5f;
        } else {
            this.speed = newSpeed;
        }
        float newHealthPoints = oldGenome.getHealthPoints() + (float) Helper.randomDoubleUpperLowerBound(0.1);
        if (newHealthPoints >= 10) {
            this.healthPoints = 10;
        } else if (newHealthPoints <= 2) {
            this.healthPoints = 2;
        } else {
            this.healthPoints = newHealthPoints;
        }
        this.antLimit = oldGenome.getAntLimit() + Helper.randomInt(40) - 20;
        this.fighterRatio = oldGenome.getFighterRatio() + (float) Helper.randomDoubleUpperLowerBound(0.5);


        this.capacity = Math.round(healthPoints / 3);
        this.attackDamage = healthPoints / 2;
        this.attackSpeed = 1 / speed;
        this.movementSpeed = speed * 2 / 3;
        this.foodConsumption = (speed * 2 + healthPoints) / 2;
    }

    public float getSpeed() {
        return speed;
    }

    public float getHealthPoints() {
        return healthPoints;
    }

    public int getAntLimit() {
        return antLimit;
    }

    public float getFighterRatio() {
        return fighterRatio;
    }

    public int getCapacity() {
        return capacity;
    }

    public float getAttackDamage() {
        return attackDamage;
    }

    public float getAttackSpeed() {
        return attackSpeed;
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }

    public float getFoodConsumption() {
        return foodConsumption;
    }

    public String getText() {
        return "Ant Limit: " + antLimit + "\nFigthers: " + 1 / fighterRatio + "%" + "\nHP: " + healthPoints + "\nCapacity: " + capacity + "\nAttack Damage " + attackDamage +
                "\nAttack Speed: " + attackSpeed + "\nMovement Speed: " + movementSpeed + "\nFood Consumption: " + foodConsumption;
    }
}
