package com.obss.softwarecrafter.model.jpa.entity;

import com.obss.softwarecrafter.model.enums.AccountStatusEnum;
import com.obss.softwarecrafter.model.enums.ProxyType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/*@EqualsAndHashCode(callSuper = true)
@Data*/
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(schema = "CRWM", name = "PROXY_SERVER")
public class ProxyServerEntity extends BaseEntity {
    private String ip;
    private int port;
    private String username;
    private String password;
    @Enumerated(EnumType.ORDINAL)
    private AccountStatusEnum status;
    private ProxyType type;
}
