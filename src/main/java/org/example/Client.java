package org.example;

import jakarta.persistence.*;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int PersonalId;
    private String firstName;
    private String lastName;

    @Version
    public int version;


    public Client(int personalId, String firstName, String lastName) {
        PersonalId = personalId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Client() {

    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public int getPersonalId() {
        return PersonalId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
