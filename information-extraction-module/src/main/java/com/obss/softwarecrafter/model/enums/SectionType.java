package com.obss.softwarecrafter.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
public enum SectionType {
    ABOUT("about"),
    EXPERIENCE("experience"),
    EDUCATION("education"),
    CERTIFICATIONS("licenses_and_certifications"),
    VOLUNTEERING("volunteering_experience"),
    SKILLS("skills"),
    AWARDS("honors_and_awards"),
    LANGUAGES("languages"),
    ORGANIZATIONS("organizations"),
    INTERESTS("interests"),
    VOLUNTEER_CAUSES("volunteer_causes"),
    PROJECTS("projects"),
    PUBLICATIONS("publications"),
    COURSES("courses"),
    UNKNOWN("")
    ;

    private final String value;

    public static SectionType getSectionType(String value){
        Optional<SectionType> optionalSectionType = Arrays.stream(SectionType.values()).filter(sectionType -> sectionType.getValue().equals(value)).findFirst();
        return optionalSectionType.orElse(SectionType.UNKNOWN);
    }


}
