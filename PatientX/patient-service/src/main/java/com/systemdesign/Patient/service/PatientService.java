package com.systemdesign.Patient.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.systemdesign.Patient.dto.PatientRequestDTO;
import com.systemdesign.Patient.dto.PatientResponseDTO;
import com.systemdesign.Patient.exception.EmailAlreadyExistsException;
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

    public PatientResponseDTO createPatient(PatientRequestDTO patientRequestDTO) {

        if(patientRepository.existsByEmail(patientRequestDTO.getEmail())) {
            throw new EmailAlreadyExistsException("A patient with this email " + patientRequestDTO.getEmail() + " already exists.");
        }

        Patient patient = PatientMapper.toModel(patientRequestDTO);
        Patient savedPatient = patientRepository.save(patient);
        return PatientMapper.toDTO(savedPatient);
    }
}
