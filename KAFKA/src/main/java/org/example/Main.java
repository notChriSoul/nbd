package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.vms.Normal;
import org.example.vms.VirtualMachine;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

import static org.example.KafkaProducent.initProducer;
import static org.example.KafkaProducent.sendRentAsync;

public class Main {


    public static void main(String[] args) throws ExecutionException, InterruptedException, JsonProcessingException {

        KafkaConsument kafkaConsument = new KafkaConsument(2);
        kafkaConsument.initConsumers();
        kafkaConsument.consumeTopicByAllConsumers();


    }
}