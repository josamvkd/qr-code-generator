package com.josamtechie.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetails {
    private String userName;
    private String mobileNo;
    private String accountType;
    private String accountNo;
}
