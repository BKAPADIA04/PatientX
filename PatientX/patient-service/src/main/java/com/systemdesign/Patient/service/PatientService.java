package com.systemdesign.Patient.service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.systemdesign.Patient.dto.PatientRequestDTO;
import com.systemdesign.Patient.dto.PatientResponseDTO;
import com.systemdesign.Patient.exception.EmailAlreadyExistsException;
import com.systemdesign.Patient.exception.PatientNotFoundException;
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

    public PatientResponseDTO updatePatient(UUID patientId, PatientRequestDTO patientRequestDTO) {
        Patient existingPatient = patientRepository.findById(patientId).orElseThrow(
            () -> new PatientNotFoundException("Patient with ID " + patientId + " does not exist.")
        );

        if(patientRepository.existsByEmailAndIdNot(patientRequestDTO.getEmail(),patientId)) {
            throw new EmailAlreadyExistsException("A patient with this email " + patientRequestDTO.getEmail() + " already exists.");
        }

        existingPatient.setName(patientRequestDTO.getName());
        existingPatient.setEmail(patientRequestDTO.getEmail());
        existingPatient.setAddress(patientRequestDTO.getAddress());
        existingPatient.setDateOfBirth(LocalDate.parse(patientRequestDTO.getDateOfBirth()));

        return PatientMapper.toDTO(patientRepository.save(existingPatient));
    }

    public void deletePatient(UUID patientId) {
        Patient existingPatient = patientRepository.findById(patientId).orElseThrow(
            () -> new PatientNotFoundException("Patient with ID " + patientId + " does not exist.")
        );
        patientRepository.delete(existingPatient);
        return;
    }
}
