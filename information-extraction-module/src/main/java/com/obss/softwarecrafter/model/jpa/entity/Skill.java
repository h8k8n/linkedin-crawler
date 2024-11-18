package com.obss.softwarecrafter.model.jpa.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(schema = "INFM", name = "SKILL")
public class Skill extends BaseEntity{
    private String name;
    private String organisation;
    private String approveCount;

//    @ManyToOne
//    @JoinColumn(name="linkedin_profile_id", referencedColumnName = "id")
//    private LinkedinProfile linkedinProfile;
}