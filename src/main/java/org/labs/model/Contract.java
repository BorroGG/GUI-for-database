package org.labs.model;

import lombok.Data;

import java.util.Date;

@Data
public class Contract {
    private Integer number;
    private Date dateConclusion;
    private String description;
    private Integer customerId;
}
