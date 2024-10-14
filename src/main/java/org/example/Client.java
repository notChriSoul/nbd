package org.example;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int PersonalId;
    @Setter
    private String firstName;
    @Setter
    private String lastName;

    @Setter
    @Version
    public int version;

    public Client(int personalId, String firstName, String lastName) {
        PersonalId = personalId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

}
