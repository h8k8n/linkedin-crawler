package com.obss.softwarecrafter.model.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(schema = "INFM", name = "EXPERIENCE")
public class Experience extends BaseEntity{
    private String title;
    private String company;
    private String duration;
    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;

//    @ManyToOne
//    @JoinColumn(name="linkedin_profile_id")
//    private LinkedinProfile linkedinProfile;
}