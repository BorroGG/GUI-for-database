package org.labs.model;

import lombok.Data;

import java.util.Date;

@Data
public class Mission {
    private Integer missionId;
    private String name;
    private String description;
    private Date beginDate;
    private Date endDate;
    private Date complete;
    private Integer author;
    private Integer executor;
    private Character priorityId;
    private Integer customerId;
}
