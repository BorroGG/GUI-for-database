package org.labs.model;

import lombok.Data;

@Data
public class Customer {
    private Integer customerId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String phone;
    private String email;
    private String inn;

}
