package com.obss.softwarecrafter.model.jpa.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(schema = "INFM", name = "AWARD")
public class Award extends BaseEntity {
    private String name;

    @Column(columnDefinition = "TEXT")
    private String info;

//    @ManyToOne
//    @JoinColumn(name="linkedin_profile_id", referencedColumnName = "id")
//    private LinkedinProfile linkedinProfile;
}