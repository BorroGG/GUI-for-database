package org.labs.model;

import lombok.Data;

@Data
public class Employee {
    private Integer serviceNumber;
    private String firstName;
    private String lastName;
    private String middleName;
    private String phone;
    private String email;
    private String fax;
    private String login;
    private String password;
    private Integer positionId;
}
