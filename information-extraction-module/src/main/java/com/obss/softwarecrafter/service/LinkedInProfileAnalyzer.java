package com.obss.softwarecrafter.service;

import com.obss.softwarecrafter.model.enums.SectionType;
import com.obss.softwarecrafter.model.jpa.entity.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class LinkedInProfileAnalyzer {

    public LinkedinProfile analyzeProfile(String htmlContent) {
        Document doc = Jsoup.parse(htmlContent);
        LinkedinProfile profile = new LinkedinProfile();

        Elements sections = doc.getElementsByTag("section");

        Map<SectionType, Element> sectionTypeElementMap = sections.stream()
                .filter(element -> !element.getElementsByClass("pv-profile-card__anchor").isEmpty())
                .filter(element -> {
                    Elements elementsByClass = element.getElementsByClass("pv-profile-card__anchor");
                    if (!elementsByClass.isEmpty()) {
                        return SectionType.getSectionType(elementsByClass.get(0).id()) != SectionType.UNKNOWN;
                    } else {
                        return false;
                    }
                })
                .collect(Collectors.toMap(
                        element -> {
                            Elements elementsByClass = element.getElementsByClass("pv-profile-card__anchor");
                            SectionType sectionType = SectionType.getSectionType(elementsByClass.get(0).id());
                            System.out.println("key: " + sectionType + " - id: " + elementsByClass.get(0).id());
                            return sectionType;
                        },
                        element -> element));
        profile.setFullName(extractFullName(doc));
        profile.setHeadline(extractHeadline(doc));
        profile.setLocation(extractLocation(doc));
        profile.setProfilePictureUrl(extractProfilePictureUrl(doc));
        profile.setConnectionCount(extractConnectionCount(doc));

        sectionTypeElementMap.forEach((sectionType, element) -> {
            try {
                switch (sectionType) {
                    case ABOUT -> profile.setAbout(extractAbout(element));
                    case AWARDS -> profile.setAwards(extractAwards(element));
                    case SKILLS -> profile.setSkills(extractSkills(element));
                    case EXPERIENCE -> profile.setExperiences(extractExperiences(element));
                    case EDUCATION -> profile.setEducations(extractEducation(element));
                    case CERTIFICATIONS -> profile.setCertifications(extractCertifications(element));
                    case VOLUNTEERING -> profile.setVolunteerings(extractVolunteering(element));
                    case LANGUAGES -> profile.setLanguages(extractLanguages(element));
                    case VOLUNTEER_CAUSES -> System.out.println("TODO VOLUNTEER_CAUSES");
                    case INTERESTS -> System.out.println("TODO INTERESTS");
                    case ORGANIZATIONS -> System.out.println("TODO ORGANIZATIONS");
                    case PROJECTS -> System.out.println("TODO PROJECTS");
                    case UNKNOWN -> System.out.println("TODO UNKNOWN");
                }
            } catch (Exception e) {
                System.out.println("section'da hata olustu: " + sectionType + " - " + e.getMessage());
            }

        });

        return profile;
    }

    private String extractFullName(Document doc) {
        Element nameElement = doc.selectFirst("h1");
        return nameElement != null ? nameElement.text() : "";
    }

    private String extractHeadline(Document doc) {
        Element headlineElement = doc.selectFirst("div.text-body-medium");
        return headlineElement != null ? headlineElement.text() : "";
    }

    private String extractLocation(Document doc) {
        Element locationElement = doc.selectFirst("span.text-body-small.inline.t-black--light.break-words");
        return locationElement != null ? locationElement.text() : "";
    }

    private String extractAbout(Element section) {
        Element element = section.getElementsByClass("display-flex").get(0);
        return element.getElementsByTag("span").stream().map(Element::text).collect(Collectors.joining());
    }

    private String extractProfilePictureUrl(Document doc) {
        Elements elementsByClass = doc.getElementsByClass("pv-top-card-profile-picture__image--show");
        if (!elementsByClass.isEmpty()) {
            return elementsByClass.get(0).attr("src");
        }
        return null;
    }

    private List<Experience> extractExperiences(Element section) {
        List<Experience> experiences = new ArrayList<>();
        if (Objects.isNull(section))
            return experiences;

        Elements experienceElements = section.getElementsByClass("artdeco-list__item");
        for (Element expElement : experienceElements) {
            Experience experience = new Experience();

            Element titleElement = expElement.selectFirst("div.align-items-center.mr1.t-bold span");
            Element companyElement = expElement.selectFirst("span.t-14.t-normal span");
            Element durationElement = expElement.selectFirst("span.t-14.t-normal.t-black--light span");
            Element locationElement = expElement.selectFirst("span.t-14.t-normal.t-black--light:last-child span");
            Element descriptionElement = expElement.selectFirst("div.pvs-entity__sub-components");


            if (titleElement != null) {
                experience.setTitle(titleElement.text());
            }
            if (companyElement != null) {
                experience.setCompany(companyElement.text());
            }
            if (durationElement != null) {
                experience.setDuration(durationElement.text());
            }
            if (locationElement != null) {
                experience.setLocation(locationElement.text());
            }
            if (descriptionElement != null) {
                experience.setDescription(descriptionElement.text());
            }
            experiences.add(experience);
        }

        return experiences;
    }

    private List<Education> extractEducation(Element section) {
        List<Education> educations = new ArrayList<>();
        if (Objects.isNull(section))
            return educations;

        Elements educationElements = section.getElementsByClass("artdeco-list__item");
        for (Element educationElement : educationElements) {
            Education education = new Education();

            Element schoolNameElement = educationElement.selectFirst("div.align-items-center.mr1.t-bold span");
            Element departmentElement = educationElement.selectFirst("span.t-14.t-normal span");
            Element durationElement = educationElement.selectFirst("span.t-14.t-normal.t-black--light span");
            Element descriptionElement = educationElement.selectFirst("div.pvs-entity__sub-components");

            if (schoolNameElement != null) {
                education.setSchoolName(schoolNameElement.text());
            }
            if (departmentElement != null) {
                education.setDepartment(departmentElement.text());
            }
            if (durationElement != null) {
                education.setDuration(durationElement.text());
            }
            if (descriptionElement != null) {
                education.setDescription(descriptionElement.text());
            }
            educations.add(education);
        }

        return educations;
    }

    private List<Certification> extractCertifications(Element section) {
        List<Certification> certifications = new ArrayList<>();
        if (Objects.isNull(section))
            return certifications;

        Elements certificationElements = section.getElementsByClass("artdeco-list__item");
        for (Element certificationElement : certificationElements) {
            Certification certification = new Certification();

            Element nameElement = certificationElement.selectFirst("div.align-items-center.mr1.t-bold span");
            Element issuerElement = certificationElement.selectFirst("span.t-14.t-normal span");
            Element durationElement = certificationElement.selectFirst("span.t-14.t-normal.t-black--light span");
            Element descriptionElement = certificationElement.selectFirst("div.pvs-entity__sub-components");

            if (nameElement != null) {
                certification.setName(nameElement.text());
            }
            if (issuerElement != null) {
                certification.setIssuer(issuerElement.text());
            }
            if (durationElement != null) {
                certification.setDuration(durationElement.text());
            }
            if (descriptionElement != null) {
                certification.setDescription(descriptionElement.text());
            }
            certifications.add(certification);
        }

        return certifications;
    }

    private List<Volunteering> extractVolunteering(Element section) {
        List<Volunteering> volunteerings = new ArrayList<>();
        if (Objects.isNull(section))
            return volunteerings;

        Elements volunteeringElements = section.getElementsByClass("artdeco-list__item");
        for (Element volunteeringElement : volunteeringElements) {
            Volunteering volunteering = new Volunteering();

            Element nameElement = volunteeringElement.selectFirst("div.align-items-center.mr1.t-bold span");
            Element organisationElement = volunteeringElement.selectFirst("span.t-14.t-normal span");
            Element durationElement = volunteeringElement.selectFirst("span.t-14.t-normal.t-black--light span");
            Element descriptionElement = volunteeringElement.selectFirst("div.pvs-entity__sub-components");

            if (nameElement != null) {
                volunteering.setName(nameElement.text());
            }
            if (organisationElement != null) {
                volunteering.setOrganisation(organisationElement.text());
            }
            if (durationElement != null) {
                volunteering.setDuration(durationElement.text());
            }
            if (descriptionElement != null) {
                volunteering.setDescription(descriptionElement.text());
            }
            volunteerings.add(volunteering);
        }

        return volunteerings;
    }

    private List<Skill> extractSkills(Element section) {
        List<Skill> skills = new ArrayList<>();
        if (Objects.isNull(section))
            return skills;

        Elements skillsElements = section.getElementsByClass("artdeco-list__item");
        for (Element skillElement : skillsElements) {
            Skill skill = new Skill();

            skillElement.select("div.pvs-entity__sub-components li span").stream()
                    .filter(element -> !"visually-hidden".equals(element.className()) && !"pvs-navigation__text".equals(element.className()))
                    .forEach(element -> {
                        String text = element.text();
                        if (text.contains("onay")) {
                            skill.setApproveCount(text);
                        } else {
                            skill.setOrganisation(text);
                        }
                    });

            Element nameElement = skillElement.selectFirst("div.align-items-center.mr1.t-bold span");

            if (nameElement != null) {
                skill.setName(nameElement.text());
            }

            skills.add(skill);
        }

        return skills;
    }

    private int extractConnectionCount(Document doc) {
        Element connectionsElement = doc.selectFirst("span.t-bold:contains(500+)");
        if (connectionsElement != null) {
            String connections = connectionsElement.text().replace("+", "");
            try {
                return Integer.parseInt(connections);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
        return 0;
    }

    private List<Award> extractAwards(Element section) {
        List<Award> awards = new ArrayList<>();
        if (Objects.isNull(section))
            return awards;

        Elements awardElements = section.getElementsByClass("artdeco-list__item");
        for (Element awardElement : awardElements) {
            Award award = new Award();

            Element nameElement = awardElement.selectFirst("div.align-items-center.mr1.t-bold span");
            Element infoElement = awardElement.selectFirst("span.t-14.t-normal span");

            if (nameElement != null) {
                award.setName(nameElement.text());
            }
            if (infoElement != null) {
                award.setInfo(infoElement.text());
            }
            awards.add(award);
        }

        return awards;
    }

    private List<Language> extractLanguages(Element section) {
        List<Language> languages = new ArrayList<>();
        if (Objects.isNull(section))
            return languages;

        Elements languageElements = section.getElementsByClass("artdeco-list__item");
        for (Element languageElement : languageElements) {
            Language language = new Language();

            Element nameElement = languageElement.selectFirst("div.align-items-center.mr1.t-bold span");
            Element infoElement = languageElement.selectFirst("span.t-14.t-normal span");

            if (nameElement != null) {
                language.setName(nameElement.text());
            }
            if (infoElement != null) {
                language.setInfo(infoElement.text());
            }
            languages.add(language);
        }

        return languages;
    }
}