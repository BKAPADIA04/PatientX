package com.systemdesign.Patient.mapper;

import java.time.LocalDate;

import com.systemdesign.Patient.dto.PatientRequestDTO;
import com.systemdesign.Patient.dto.PatientResponseDTO;
import com.systemdesign.Patient.model.Patient;

public class PatientMapper {
    public static PatientResponseDTO toDTO(Patient patient) {
        PatientResponseDTO dto = new PatientResponseDTO();
        dto.setId(patient.getId().toString());
        dto.setName(patient.getName());
        dto.setEmail(patient.getEmail());
        dto.setAddress(patient.getAddress());
        dto.setDateOfBirth(patient.getDateOfBirth().toString());

        return dto;
    }

    public static Patient toModel(PatientRequestDTO patientRequestDTO) {
        Patient patient = new Patient();
        patient.setName(patientRequestDTO.getName());
        patient.setEmail(patientRequestDTO.getEmail());
        patient.setAddress(patientRequestDTO.getAddress());
        patient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));
        patient.setRegisteredDate(LocalDate.parse(patientRequestDTO.getRegisteredDate()));
        
        return patient;
    }
}
