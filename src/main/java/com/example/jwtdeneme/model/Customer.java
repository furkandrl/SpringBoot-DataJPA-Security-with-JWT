package com.example.jwtdeneme.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String username;
    private String email;
    private String password;
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    private List<Address> addresses = new ArrayList<>();
    @Enumerated(EnumType.STRING)
    private Role role;

    @DateTimeFormat(pattern = "dd-MM-yyyy")
    private LocalDate birthday;

    @Transient
    private int age;

    public int getAge() {
        LocalDate currentDate = LocalDate.now();
        return Period.between(birthday,  currentDate).getYears();
    }
}
