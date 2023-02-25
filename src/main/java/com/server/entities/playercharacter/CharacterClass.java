package com.server.entities.playercharacter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class CharacterClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private int hitDie;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getHitDie() {
        return hitDie;
    }
}
