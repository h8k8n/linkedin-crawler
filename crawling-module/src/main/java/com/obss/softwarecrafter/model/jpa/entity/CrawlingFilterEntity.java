package com.obss.softwarecrafter.model.jpa.entity;

import com.obss.softwarecrafter.model.enums.FilterTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(schema = "CRWM", name = "CRAWLING_FILTER")
public class CrawlingFilterEntity extends BaseEntity {

    private String country;
    private String title;
    private String workplace;
    private String school;
    private String educationLevel;
    private String department;
}
