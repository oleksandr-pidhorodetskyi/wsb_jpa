package com.jpacourse.dto;


import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VisitTO implements Serializable
{
    private Long id;

    private LocalDateTime time;

    private String firstNameOfDoctor;

    private String lastNameOfDoctor;

    private List<MedicalTreatmentTO> treatments = new ArrayList<>();;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getFirstNameOfDoctor() { return firstNameOfDoctor; }

    public void setFirstNameOfDoctor(String firstNameOfDoctor) { this.firstNameOfDoctor = firstNameOfDoctor; }

    public String getLastNameOfDoctor() { return lastNameOfDoctor; }

    public void setLastNameOfDoctor(String lastNameOfDoctor) { this.lastNameOfDoctor = lastNameOfDoctor; }

    public LocalDateTime getTime() {
        return time;
    }

    public List<MedicalTreatmentTO> getTreatments() { return treatments; }

    public void setTreatments(List<MedicalTreatmentTO> treatments) { this.treatments = treatments; }

}
