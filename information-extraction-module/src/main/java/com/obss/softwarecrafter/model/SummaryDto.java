package com.obss.softwarecrafter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SummaryDto implements Serializable {
    private LinkedinProfileSummaryDto summary;
    private ActionsDto actions;
}
