package org.example;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class Client {


    private int PersonalId;
    @Setter
    private String firstName;
    @Setter
    private String lastName;


    public Client(int personalId, String firstName, String lastName) {
        PersonalId = personalId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
