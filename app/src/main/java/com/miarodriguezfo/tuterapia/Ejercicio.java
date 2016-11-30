package com.miarodriguezfo.tuterapia;

/**
 * Created by Miguel Rodriguez on 31/10/2015.
 */
public class Ejercicio {
    String uid;
    String name;
    String category;
    String injury;
    String description;
    String equipment;
    String creator;

    public Ejercicio(String uid, String name, String category, String injury, String description, String creator, String equipment) {
        this.uid = uid;
        this.name = name;
        this.category = category;
        this.injury = injury;
        this.description = description;
        this.creator = creator;
        this.equipment = equipment;
    }

    public String getUid() {
        return uid;
    }
}
