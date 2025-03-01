
package  org.example;

import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.codecs.pojo.annotations.BsonId;

@Getter
public class Client {


    @BsonId
    private String personalID;

    @Setter
    @BsonProperty("firstName")
    private String firstName;

    @Setter
    @BsonProperty("lastName")
    private String lastName;

    @BsonProperty("currentRentsNumber")
    private int currentRentsNumber;

    public Client() {
    }

    @BsonCreator
    public Client(@BsonProperty("personalID") String personalID,
                  @BsonProperty("firstName") String firstName,
                  @BsonProperty("lastName") String lastName)
    {
        this.personalID = personalID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.currentRentsNumber = 0;
    }
}