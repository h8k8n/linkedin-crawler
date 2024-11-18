package com.obss.softwarecrafter.model.jpa.entity;

import com.obss.softwarecrafter.model.enums.CrawlingJobStatus;
import com.obss.softwarecrafter.model.enums.CrawlingTypeEnum;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(schema = "CRWM", name = "CRAWLING_MASTER")
public class CrawlingMasterEntity extends BaseEntity{
    private String description;//frontend'te name yaptık, güncellencek

    @Enumerated(EnumType.ORDINAL)
    private CrawlingTypeEnum type;

    private String target;

    private int threadCount;

    private boolean recursiveEnable;

    private int recursiveDepth;

    @Enumerated(EnumType.ORDINAL)
    private CrawlingJobStatus status;

    private String filters;


}
