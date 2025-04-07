package com.scu.aicontractsummarizerdemo.dto;
import com.scu.aicontractsummarizerdemo.entity.Role;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class RegisterRequest {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Role role;
    private BorrowerDetails borrowerDetails; // For Borrowers
    private LenderDetails lenderDetails; // For Lenders
}
