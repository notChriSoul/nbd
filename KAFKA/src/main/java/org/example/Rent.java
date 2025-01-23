package org.example;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.example.vms.VirtualMachine;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

@Getter
@NoArgsConstructor
@JsonTypeInfo(
        use = JsonTypeInfo.Id.CLASS,
        include = JsonTypeInfo.As.PROPERTY,
        property = "@class"
)
public class Rent {

    @BsonId
    private int id;

    @Setter
    @BsonProperty("client")
    private Client client;

    @Setter
    @BsonProperty("vm")
    private VirtualMachine VM;

    @Setter
    @BsonProperty("beginTime")
    private LocalDateTime beginTime;


    @Setter
    @BsonProperty("endTime")
    private LocalDateTime endTime;

    @Setter
    @BsonProperty("rentCost")
    private double rentCost;


    private String nazwa = "nazwa";

    @BsonCreator
    @JsonCreator
    public Rent(@BsonProperty("id") @JsonProperty("id")int id,
                @BsonProperty("client") @JsonProperty("client") Client client,
                @BsonProperty("vm") @JsonProperty("vm") VirtualMachine VM,
                @BsonProperty("beginTime") @JsonProperty("beginTime") LocalDateTime beginTime) {
        this.id = id;
        this.client = client;
        this.VM = VM;
        this.beginTime = Objects.requireNonNullElseGet(beginTime, LocalDateTime::now).withNano(0);
    }


}
