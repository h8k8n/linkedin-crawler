package com.obss.softwarecrafter.model.jpa.entity;

import com.obss.softwarecrafter.model.enums.ProcessStatusEnum;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(schema = "INFM", name = "LINKEDIN_PROFILE")
public class LinkedinProfile extends BaseEntity {
    private String profileId;
    private String fullName;
    private String headline;
    private String location;
    private String profilePictureUrl;
    private int connectionCount;
    private LocalDateTime crawlDate;

    @Enumerated(EnumType.ORDINAL)
    private ProcessStatusEnum status;

    @Column(columnDefinition = "TEXT")
    private String about;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "linkedin_profile_id")
    private List<Experience> experiences;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "linkedin_profile_id")
    private List<Education> educations;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "linkedin_profile_id")
    private List<Certification> certifications;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "linkedin_profile_id")
    private List<Volunteering> volunteerings;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "linkedin_profile_id")
    private List<Skill> skills;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "linkedin_profile_id")
    private List<Award> awards;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "linkedin_profile_id")
    private List<Language> languages;
}