package com.obss.softwarecrafter.model.jpa.repository;

import com.obss.softwarecrafter.model.jpa.entity.CrawlingResultEntity;
import com.obss.softwarecrafter.utilities.DateUtils;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;

public class CrawlingResultSpecification {

    public static Specification<CrawlingResultEntity> withFilters(String[] fullName,
                                                                  Integer status,
                                                                  String crawlDateBegin,
                                                                  String crawlDateEnd,
                                                                  String processDateBegin,
                                                                  String processDateEnd) {
        return Specification
                .where(nameContains(fullName))
                .and(statusEquals(status))
                .and(betweenCrawlDate(crawlDateBegin, crawlDateEnd))
                .and(betweenProcessDate(processDateBegin, processDateEnd));
    }

    private static Specification<CrawlingResultEntity> nameContains(String[] fullName) {
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

    private static Specification<CrawlingResultEntity> statusEquals(Integer status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return null;
            }
            if (status == 0) {
                return criteriaBuilder.isNull(root.get("processDate"));
            } else {
                return criteriaBuilder.isNotNull(root.get("processDate"));
            }
        };
    }

    private static Specification<CrawlingResultEntity> betweenCrawlDate(String crawlDateBegin, String crawlDateEnd) {
        return (root, query, criteriaBuilder) -> {
            if ((crawlDateBegin == null || crawlDateBegin.isEmpty()) || (crawlDateEnd == null || crawlDateEnd.isEmpty())) {
                return null;
            }

            return criteriaBuilder.between(root.get("crawlDate"),
                    DateUtils.parse(crawlDateBegin), DateUtils.parse(crawlDateEnd)
                    );
        };
    }

    private static Specification<CrawlingResultEntity> betweenProcessDate(String processDateBegin, String processDateEnd) {
        return (root, query, criteriaBuilder) -> {
            if ((processDateBegin == null || processDateBegin.isEmpty()) || (processDateEnd == null || processDateEnd.isEmpty())) {
                return null;
            }

            return criteriaBuilder.between(root.get("processDate"),
                    DateUtils.parse(processDateBegin), DateUtils.parse(processDateEnd)
            );
        };
    }
}
