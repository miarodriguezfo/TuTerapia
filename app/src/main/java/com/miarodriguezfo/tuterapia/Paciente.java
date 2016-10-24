package com.miarodriguezfo.tuterapia;

/**
 * Created by Miguel Rodriguez on 31/10/2015.
 */
public class Paciente {
    String uid;
    String name;
    String email;
    String photoUrl;

    Paciente(String pName, String pEmail,  String url, String uid) {
        this.name = pName;
        this.email = pEmail;
        this.photoUrl = url;
        this.uid=uid;
    }

    public String getUid() {
        return uid;
    }
}
