package com.obss.softwarecrafter.model.jpa.entity;

import com.obss.softwarecrafter.model.enums.AccountStatusEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(schema = "CRWM", name = "LINKEDIN_ACCOUNT")
public class LinkedinAccountEntity extends BaseEntity {
    private String username;
    private String password;
    @Enumerated(EnumType.ORDINAL)
    private AccountStatusEnum status;
}
