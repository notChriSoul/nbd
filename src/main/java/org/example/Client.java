package org.example;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int PersonalId;
    private String firstName;
    private String lastName;


    public Client(int personalId, String firstName, String lastName) {
        PersonalId = personalId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // why?
    // it doesn't let me do it paremeterless as long as id is "final"
    public Client() {

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
