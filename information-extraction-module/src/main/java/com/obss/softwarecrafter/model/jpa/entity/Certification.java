package com.obss.softwarecrafter.model.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(schema = "INFM", name = "CERTIFICATION")
public class Certification extends BaseEntity{
    private String name;
    private String issuer;
    private String duration;

    @Column(columnDefinition = "TEXT")
    private String description;

//    @ManyToOne
//    @JoinColumn(name="linkedin_profile_id", referencedColumnName = "id")
//    private LinkedinProfile linkedinProfile;

}