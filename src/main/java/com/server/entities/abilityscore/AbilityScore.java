package com.server.entities.abilityscore;

import com.server.entities.monster.Monster;
import jakarta.persistence.*;

@MappedSuperclass
public abstract class AbilityScore {
    private int id;
    private int score = 10;
    private boolean proficient = false;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getBonus() {
        return (int) Math.floor((score - 10) / 2.0);
    }

    /**
     * This method does nothing because the bonus is a calculated value, it cannot be set. But JPA complains if this
     * method is not included, so here it is
     * @param bonus
     */
    public void setBonus(int bonus) { }

    public boolean isProficient() {
        return proficient;
    }

    public void setProficient(boolean proficient) {
        this.proficient = proficient;
    }
}
