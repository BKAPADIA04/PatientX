package com.systemdesign.Patient.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.systemdesign.Patient.dto.PatientResponseDTO;
import com.systemdesign.Patient.mapper.PatientMapper;
import com.systemdesign.Patient.model.Patient;
import com.systemdesign.Patient.repository.PatientRepository;

@Service
public class PatientService {
    private final PatientRepository patientRepository;

    public PatientService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    public List<PatientResponseDTO> getPatients() {
        List<Patient> patients = patientRepository.findAll();
        List<PatientResponseDTO> patientDTOs = patients.stream()
            .map(PatientMapper::toDTO)
            .toList();
        
        return patientDTOs;
    }
}
