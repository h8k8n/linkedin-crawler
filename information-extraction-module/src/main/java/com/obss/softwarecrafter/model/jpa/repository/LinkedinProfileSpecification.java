package com.obss.softwarecrafter.model.jpa.repository;

import com.obss.softwarecrafter.model.jpa.entity.Education;
import com.obss.softwarecrafter.model.jpa.entity.Experience;
import com.obss.softwarecrafter.model.jpa.entity.LinkedinProfile;
import com.obss.softwarecrafter.utilities.DateUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class LinkedinProfileSpecification {

    public static Specification<LinkedinProfile> withFilters(String[] fullName,
                                                             String crawlDateBegin,
                                                             String crawlDateEnd,
                                                             String processDateBegin,
                                                             String processDateEnd,
                                                             String[] companies,
                                                             String[] titles,
                                                             String[] schools,
                                                             String[] departments) {
        return Specification
                .where(nameContains(fullName))
                .and(betweenCrawlDate(crawlDateBegin, crawlDateEnd))
                .and(betweenProcessDate(processDateBegin, processDateEnd))
                .and(companyAndTitleContains(companies, titles))
                .and(schoolAndDepartmentContains(schools, departments));
    }

    private static Specification<LinkedinProfile> nameContains(String[] fullName) {
        return (root, query, criteriaBuilder) -> {
            if (ArrayUtils.isEmpty(fullName)) {
                return null;
            }

            List<Predicate> predicates = Arrays.stream(fullName)
                    .map(name -> criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("fullName")),
                            "%" + name.toLowerCase() + "%"
                    ))
                    .toList();

            return criteriaBuilder.or(predicates.toArray(new Predicate[0]));
        };
    }

    private static Specification<LinkedinProfile> betweenCrawlDate(String crawlDateBegin, String crawlDateEnd) {
        return (root, query, criteriaBuilder) -> {
            if ((crawlDateBegin == null || crawlDateBegin.isEmpty()) || (crawlDateEnd == null || crawlDateEnd.isEmpty())) {
                return null;
            }

            return criteriaBuilder.between(root.get("crawlDate"),
                    DateUtils.parse(crawlDateBegin), DateUtils.parse(crawlDateEnd)
                    );
        };
    }

    private static Specification<LinkedinProfile> betweenProcessDate(String processDateBegin, String processDateEnd) {
        return (root, query, criteriaBuilder) -> {
            if ((processDateBegin == null || processDateBegin.isEmpty()) || (processDateEnd == null || processDateEnd.isEmpty())) {
                return null;
            }

            return criteriaBuilder.between(root.get("lastModifiedDate"),
                    DateUtils.parse(processDateBegin), DateUtils.parse(processDateEnd)
            );
        };
    }

    private static Specification<LinkedinProfile> companyAndTitleContains(String[] companies, String[] titles) {
        return (root, query, criteriaBuilder) -> {
            if (ArrayUtils.isEmpty(companies) && ArrayUtils.isEmpty(titles)) {
                return null;
            }

            if(Objects.nonNull(query)) {
                query.distinct(true);
            }

            Join<LinkedinProfile, Experience> experienceJoin = root.join("experiences", JoinType.INNER);
            List<Predicate> allPredicates = new ArrayList<>();

            if (!ArrayUtils.isEmpty(companies)) {
                List<Predicate> companyPredicates = Arrays.stream(companies)
                        .map(company -> criteriaBuilder.like(
                                criteriaBuilder.lower(experienceJoin.get("company")),
                                "%" + company.toLowerCase() + "%"
                        ))
                        .toList();
                allPredicates.add(criteriaBuilder.or(companyPredicates.toArray(new Predicate[0])));
            }

            if (!ArrayUtils.isEmpty(titles)) {
                List<Predicate> titlePredicates = Arrays.stream(titles)
                        .map(title -> criteriaBuilder.like(
                                criteriaBuilder.lower(experienceJoin.get("title")),
                                "%" + title.toLowerCase() + "%"
                        ))
                        .toList();
                allPredicates.add(criteriaBuilder.or(titlePredicates.toArray(new Predicate[0])));
            }

            return allPredicates.size() > 1
                    ? criteriaBuilder.and(allPredicates.toArray(new Predicate[0]))
                    : allPredicates.get(0);
        };
    }

    private static Specification<LinkedinProfile> schoolAndDepartmentContains(String[] schools, String[] departments) {
        return (root, query, criteriaBuilder) -> {
            if (ArrayUtils.isEmpty(schools) && ArrayUtils.isEmpty(departments)) {
                return null;
            }

            if(Objects.nonNull(query)) {
                query.distinct(true);
            }

            Join<LinkedinProfile, Education> educationJoin = root.join("educations", JoinType.INNER);
            List<Predicate> allPredicates = new ArrayList<>();

            if (!ArrayUtils.isEmpty(schools)) {
                List<Predicate> schoolPredicates = Arrays.stream(schools)
                        .map(school -> criteriaBuilder.like(
                                criteriaBuilder.lower(educationJoin.get("schoolName")),
                                "%" + school.toLowerCase() + "%"
                        ))
                        .toList();
                allPredicates.add(criteriaBuilder.or(schoolPredicates.toArray(new Predicate[0])));
            }

            if (!ArrayUtils.isEmpty(departments)) {
                List<Predicate> departmentPredicates = Arrays.stream(departments)
                        .map(department -> criteriaBuilder.like(
                                criteriaBuilder.lower(educationJoin.get("department")),
                                "%" + department.toLowerCase() + "%"
                        ))
                        .toList();
                allPredicates.add(criteriaBuilder.or(departmentPredicates.toArray(new Predicate[0])));
            }

            return allPredicates.size() > 1
                    ? criteriaBuilder.and(allPredicates.toArray(new Predicate[0]))
                    : allPredicates.get(0);
        };
    }
}