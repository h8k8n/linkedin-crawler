package com.obss.softwarecrafter.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActionsDto implements Serializable {
    private boolean crawlJobActive;
    private boolean processJobActive;
}
