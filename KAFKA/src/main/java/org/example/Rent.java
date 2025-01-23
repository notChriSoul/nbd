package org.example;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.example.vms.VirtualMachine;

import java.time.Duration;
import java.util.Objects;
import java.util.UUID;

@Getter
@NoArgsConstructor
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
    public Rent(@BsonProperty("id") int id,
                @BsonProperty("client") Client client,
                @BsonProperty("vm") VirtualMachine VM,
                @BsonProperty("beginTime") LocalDateTime beginTime) {
        this.id = id;
        this.client = client;
        this.VM = VM;
        this.beginTime = Objects.requireNonNullElseGet(beginTime, LocalDateTime::now).withNano(0);
    }


}
