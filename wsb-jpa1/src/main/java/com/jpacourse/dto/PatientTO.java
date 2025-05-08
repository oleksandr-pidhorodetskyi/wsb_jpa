package com.jpacourse.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public class PatientTO implements Serializable {
    private Long id;
    private String firstName;
    private String lastName;
    private String telephoneNumber;
    private String email;
    private String patientNumber;
    private LocalDate dateOfBirth;
    private boolean active; // dodatkowe pole
    private List<VisitTO> visits;
    private List<VisitTO> pastVisits;
    private int version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getTelephoneNumber() {
        return telephoneNumber;
    }

    public void setTelephoneNumber(String telephoneNumber) {
        this.telephoneNumber = telephoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPatientNumber() {
        return patientNumber;
    }

    public void setPatientNumber(String patientNumber) {
        this.patientNumber = patientNumber;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public List<VisitTO> getPastVisits() {
        return pastVisits;
    }

    public void setPastVisits(List<VisitTO> pastVisits) {
        this.pastVisits = pastVisits;
    }

    public List<VisitTO> getVisits() {
        return visits;
    }

    public void setVisits(List<VisitTO> visits) {
        this.visits = visits;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }
}
