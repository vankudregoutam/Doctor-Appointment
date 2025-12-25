package com.tek;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.tek.entity.Role;
import com.tek.entity.User;
import com.tek.repository.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class DoctorAppointmentApplication {

	public static void main(String[] args) {
		SpringApplication.run(DoctorAppointmentApplication.class, args);
	}

    @Bean
    public CommandLineRunner initAdmins(UserRepo userRepository, PasswordEncoder passwordEncoder) {
        return args -> {

            long adminCount = userRepository.countByRole(Role.ADMIN);

            // If no admins exist, create first two admin accounts
            if (adminCount == 0) {

                User admin1 = new User();
                admin1.setUserName("Malik Arjun");
                admin1.setUserEmail("arjunmalik@gmail.com");
                admin1.setUserPassword(passwordEncoder.encode("mAlIk@123"));
                admin1.setPhoneNumber("9874561230");
                admin1.setGender("Male");
                admin1.setUserAge(25);
                admin1.setRole(Role.ADMIN);
                admin1.setCreatedEntity("SYSTEM", null);

                User admin2 = new User();
                admin2.setUserName("Manjunath");
                admin2.setUserEmail("manjunath@gmail.com");
                admin2.setUserPassword(passwordEncoder.encode("mAnJuNaTh@123"));
                admin2.setPhoneNumber("8976541230");
                admin2.setGender("Male");
                admin2.setUserAge(27);
                admin2.setRole(Role.ADMIN);
                admin2.setCreatedEntity("SYSTEM", null);

                userRepository.save(admin1);
                userRepository.save(admin2);

                System.out.println("✔ First two admin accounts created successfully.");
            } else {
                System.out.println("✔ Admin accounts already exist. Skipping admin creation.");
            }
        };
    }

    @Bean
    public ObjectMapper objectMapper() {

        ObjectMapper mapper = new ObjectMapper();

        mapper.registerModule(new JavaTimeModule());

        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(SerializationFeature.WRITE_DATE_TIMESTAMPS_AS_NANOSECONDS);

        // Remove all null fields from JSON automatically
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // Optional: pretty print responses
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        return mapper;
    }

}
