package com.jpacourse.persistance.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "VISIT")
public class VisitEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String description;

	@Column(nullable = false)
	private LocalDateTime time;

	@ManyToOne(optional = false, fetch = FetchType.LAZY) // Dwustronna relacja (VisitEntity - DoctorEntity)
	@JoinColumn(name = "DOCTOR_ID")
	private DoctorEntity doctor;

	@ManyToOne(optional = false, fetch = FetchType.LAZY) // Dwustronna relacja (VisitEntity - PatientEntity)
	@JoinColumn(name = "PATIENT_ID")
	private PatientEntity patient;

	@OneToMany(mappedBy = "visit", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	// Dwustronna relacja (VisitEntity - MedicalTreatmentEntity)
	private List<MedicalTreatmentEntity> treatments = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}

	public DoctorEntity getDoctor() {
		return doctor;
	}

	public void setDoctor(DoctorEntity doctor) {
		this.doctor = doctor;
	}

	public PatientEntity getPatient() {
		return patient;
	}

	public void setPatient(PatientEntity patient) {
		this.patient = patient;

		if (patient != null && !patient.getVisits().contains(this)) {
			patient.getVisits().add(this); // 👈 zapewni spójność relacji
		}
	}


	public List<MedicalTreatmentEntity> getMedicalTreatments() {
		return treatments;
	}

	public void setMedicalTreatments(List<MedicalTreatmentEntity> treatments) {
		this.treatments = treatments;
	}

}
