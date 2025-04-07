package com.scu.aicontractsummarizerdemo.service;

import com.scu.aicontractsummarizerdemo.dto.BorrowerDetails;
import com.scu.aicontractsummarizerdemo.dto.LenderDetails;
import com.scu.aicontractsummarizerdemo.entity.*;
import com.scu.aicontractsummarizerdemo.repository.BorrowerRepository;
import com.scu.aicontractsummarizerdemo.repository.LenderRepository;
import com.scu.aicontractsummarizerdemo.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final BorrowerRepository borrowerRepository;
    private final LenderRepository lenderRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository, BorrowerRepository borrowerRepository, LenderRepository lenderRepository) {
        this.userRepository = userRepository;
        this.borrowerRepository = borrowerRepository;
        this.lenderRepository = lenderRepository;
    }
    public boolean existsByEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @Transactional
    public User registerUser(String email, String password, String firstName, String lastName, String phone, Role role) {
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // Hash password
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phone);
        user.setRole(role);
        return userRepository.save(user);
    }

    @Transactional
    public void createBorrowerDetails(User user, BorrowerDetails request) {
        Borrower borrower = new Borrower();
        borrower.setUser(user);
        borrower.setBusinessName(request.getBusinessName());
        borrower.setBusinessType(request.getBusinessType());
        borrower.setAnnualRevenue(request.getAnnualRevenue());
        borrower.setYearEstablished(request.getYearEstablished());
        borrower.setEmployeeCount(request.getEmployeeCount());
        borrower.setIndustry(request.getIndustry());

        borrowerRepository.save(borrower);
    }
    @Transactional
    public void createLenderDetails(User user, LenderDetails request) {
        Lender lender = new Lender();
        lender.setUser(user);
        lender.setOrganizationType(request.getOrganizationType());
        lender.setCompanyName(request.getCompanyName());
        lender.setPreferredIndustries(
                String.join(",", request.getPreferredIndustries())); // Convert List to CSV
        lender.setMaxLoanAmount(request.getMaxLoanAmount());
        lender.setRiskTolerance(RiskTolerance.valueOf(request.getRiskTolerance().toUpperCase()));
        lender.setPreferredLoanTerms(
                String.join(",", request.getPreferredLoanTerms())); // Convert List to CSV

        lenderRepository.save(lender);
    }

    public Optional<User> authenticateUser(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));  // Match with BCrypt
    }
}
