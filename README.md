# 🏥 Doctor Appointment Management System

A role-based **Doctor Appointment Management System** built using **Spring Boot**, following clean backend architecture principles.
The application enables **patients to book appointments**, **doctors to manage schedules and leaves**, and **admins to oversee the entire system**, all secured via **Spring Security**.

This project reflects **real-world backend engineering practices** expected from a **3–5 years experienced Java backend developer**.

---

## 📌 Key Highlights

- Clean layered architecture (Controller → Service → Repository)
- Role-based authentication and authorization
- DTO-based API design (no entity exposure)
- Input validation using Jakarta Validation
- Centralized constants and clean response handling
- Interview-ready, enterprise-style backend project

---

## 🧱 Architecture Overview

Client → Controller → Service → Repository → Database

- Separation of concerns
- Maintainable & scalable
- Clean ownership of responsibilities

---

## 🛠️ Tech Stack

- Java
- Spring Boot
- Spring MVC
- Spring Security
- Spring Data JPA
- Jakarta Validation
- Maven
- Lombok

---

## 📂 Project Structure

Doctor_Appointment/
- controller
- service
- repository
- entity
- dto
- config
- constants

---

## 🔐 Security Configuration

```java
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/doctor/**").hasRole("DOCTOR")
                .requestMatchers("/api/patient/**").hasRole("PATIENT")
                .anyRequest().authenticated()
            )
            .httpBasic();
        return http.build();
    }
}
```

---

## ⚙️ Run Instructions

```bash
mvn clean install
mvn spring-boot:run
```

---

## 👨‍💻 Author

Java Backend Developer  
Spring Boot | REST APIs | Security | Clean Architecture
