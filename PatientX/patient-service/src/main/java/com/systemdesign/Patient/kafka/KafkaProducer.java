package com.systemdesign.Patient.kafka;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.systemdesign.Patient.model.Patient;

import patient.event.PatientEvent;

@Service
public class KafkaProducer {

    private static final org.slf4j.Logger Logger = org.slf4j.LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(Patient patient) {
        PatientEvent event = PatientEvent.newBuilder()
                .setPatientId(patient.getId().toString())
                .setName(patient.getName())
                .setEmail(patient.getEmail())
                .setEventType("PATIENT_CREATED")
                .build();

        try {
            Logger.info("Sending event to Kafka: {}", event);
            kafkaTemplate.send("patient", event.toByteArray());
            Logger.info("Event sent successfully");
        }
        catch (Exception e) {
            // Log the error or handle it as needed
            Logger.error("Error sending event to Kafka: " + e.getMessage());
        }
    }
}
