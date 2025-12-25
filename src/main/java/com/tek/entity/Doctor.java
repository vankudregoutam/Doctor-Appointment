package com.tek.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@Inheritance
public class Doctor extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doctorId")
    @SequenceGenerator(name = "doctorId", initialValue = 201, allocationSize = 1)
    private Integer doctorId;
    private String doctorEmail;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String doctorPassword;
    private String doctorName;
    private String specification;
    private String phoneNumber;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Appointment> appointments;

    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Leave> leaves;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_DOCTOR"));
    }

    @Override
    public String getPassword() {
        return doctorPassword;
    }

    @Override
    public String getUsername() {
        return doctorEmail;
    }
}
