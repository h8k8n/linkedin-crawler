package com.obss.softwarecrafter.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SingleOperationStatusDto {
    private String profileId;
    private boolean crawlJobActive;
}
