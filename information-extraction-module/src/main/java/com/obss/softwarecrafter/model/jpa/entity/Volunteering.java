package com.obss.softwarecrafter.model.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(schema = "INFM", name = "VOLUNTEERING")
public class Volunteering extends BaseEntity {
    private String name;
    private String organisation;
    private String duration;

    @Column(columnDefinition = "TEXT")
    private String description;

//    @ManyToOne
//    @JoinColumn(name="linkedin_profile_id", referencedColumnName = "id")
//    private LinkedinProfile linkedinProfile;

}