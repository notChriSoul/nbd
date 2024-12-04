
package  org.example;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.json.bind.annotation.JsonbTransient;
import lombok.Getter;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.bson.codecs.pojo.annotations.BsonId;

@Getter
public class Client {


    @BsonId
    @JsonbProperty("personalID")
    private String personalID;

    @Setter
    @BsonProperty("firstName")
    @JsonbProperty("firstName")
    private String firstName;

    @Setter
    @BsonProperty("lastName")
    @JsonbProperty("lastName")
    private String lastName;

    @BsonProperty("currentRentsNumber")
    @JsonbProperty("currentRentsNumber")
    private int currentRentsNumber;

    public Client() {
    }

    @BsonIgnore
    @JsonbTransient
    public String getId() {
        return getPersonalID();
    }

    @BsonCreator
    @JsonbCreator
    public Client(@BsonProperty("personalID") @JsonbProperty("personalID") String personalID,
                  @BsonProperty("firstName") @JsonbProperty("firstName") String firstName,
                  @BsonProperty("lastName")  @JsonbProperty("lastName") String lastName)
    {
        this.personalID = personalID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.currentRentsNumber = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (!(o instanceof Client client)) return false;

        return new org.apache.commons.lang3.builder.EqualsBuilder().append(getCurrentRentsNumber(), client.getCurrentRentsNumber()).append(getPersonalID(), client.getPersonalID()).append(getFirstName(), client.getFirstName()).append(getLastName(), client.getLastName()).isEquals();
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37).append(getPersonalID()).append(getFirstName()).append(getLastName()).append(getCurrentRentsNumber()).toHashCode();
    }
}