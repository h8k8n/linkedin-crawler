package com.obss.softwarecrafter.model.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(schema = "INFM", name = "EDUCATION")
public class Education extends BaseEntity{
    private String schoolName;
    private String department;
    private String duration;

    @Column(columnDefinition = "TEXT")
    private String description;

//    @ManyToOne
//    @JoinColumn(name="linkedin_profile_id", referencedColumnName = "id")
//    private LinkedinProfile linkedinProfile;
}