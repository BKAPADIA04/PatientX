package com.systemdesign.analytics_service.kafka;

import org.slf4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.google.protobuf.InvalidProtocolBufferException;

import patient.event.PatientEvent;

@Service
public class KafkaConsumer {

    private static final Logger Logger = org.slf4j.LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "patient", groupId = "analytics-service")
    public void consumeEvent(byte[] event) {
        // Logic to process the consumed message
        try {
            PatientEvent patientEvent = PatientEvent.parseFrom(event);
            // Business Logic
            Logger.info("Received PatientEvent: [patientId: {}, name: {}, eventType: {}]",
                    patientEvent.getPatientId(), patientEvent.getName(),patientEvent.getEventType());
        } catch (InvalidProtocolBufferException e) {
            Logger.error("Failed to parse PatientEvent from byte array", e.getMessage());
        }
    }
}
