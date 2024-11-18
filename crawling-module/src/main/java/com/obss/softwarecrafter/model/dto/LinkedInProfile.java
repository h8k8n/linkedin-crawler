package com.obss.softwarecrafter.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkedInProfile {
    private String fullName;
    private String headline;
    private String location;
    private String about;
    private List<String> experiences;
}